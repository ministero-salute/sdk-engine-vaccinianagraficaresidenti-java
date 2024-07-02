/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag.tracciato;

import it.mds.sdk.flusso.avn.residenti.anag.parser.regole.RecordDtoAvnResidentiAnag;
import it.mds.sdk.flusso.avn.residenti.anag.parser.regole.conf.ConfigurazioneFlussoAvnResidentiAnag;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.InformazioniAnagrafiche;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.Modalita;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.ObjectFactory;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.factory.GestoreFileFactory;
import it.mds.sdk.libreriaregole.tracciato.TracciatoSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("tracciatoSplitterAvnResAnag")
public class TracciatoSplitterImpl implements TracciatoSplitter<RecordDtoAvnResidentiAnag> {

    @Override
    public List<Path> dividiTracciato(Path tracciato) {
        return null;
    }

    @Override
    public List<Path> dividiTracciato(List<RecordDtoAvnResidentiAnag> records, String idRun) {

        try {
            ConfigurazioneFlussoAvnResidentiAnag conf = getConfigurazione();

            //Imposto gli attribute element
            String modalita = records.get(0).getModalita();
            String codiceRegione = records.get(0).getCodRegione();

            //XML ANAGRAFICA
            ObjectFactory objAnag = getObjectFactory();
            InformazioniAnagrafiche informazioniAnagrafiche = objAnag.createInformazioniAnagrafiche();

            informazioniAnagrafiche.setCodiceRegione(codiceRegione);
            informazioniAnagrafiche.setModalita(Modalita.fromValue(modalita));


            for (RecordDtoAvnResidentiAnag r : records) {
                if (!r.getTipoTrasmissioneAnagrafica().equalsIgnoreCase("NM")) {
                    creaAnagraficaXml(r, informazioniAnagrafiche, objAnag);
                }
            }

            //recupero il path del file xsd di anagrafica
            URL urlAnagXsd = this.getClass().getClassLoader().getResource("AVX.xsd");
            log.debug("URL dell'XSD per la validazione idrun {} : {}", idRun, urlAnagXsd);

            //scrivi XML ANAGRAFICA
            GestoreFile gestoreFile = GestoreFileFactory.getGestoreFile("XML");
            String pathAnagraf = conf.getXmlOutput().getPercorso() + "SDK_AVN_AVX_" + records.get(0).getCampiInput().getPeriodoRiferimentoInput() + "_" + idRun + ".xml";
            //gestoreFile.scriviDto(informazioniAnagrafiche, pathAnagraf, urlAnagXsd);


            return List.of(Path.of(pathAnagraf));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            log.error("[{}].dividiTracciato  - records[{}]  - idRun[{}] -" + e.getMessage(),
                    this.getClass().getName(),
                    records.stream().map(obj -> "" + obj.toString()).collect(Collectors.joining("|")),
                    idRun,
                    e
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossibile validare il csv in ingresso. message: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public ConfigurazioneFlussoAvnResidentiAnag getConfigurazione() {
        return new ConfigurazioneFlussoAvnResidentiAnag();
    }

    public ObjectFactory getObjectFactory() {
        return new ObjectFactory();
    }

    private void creaAnagraficaXml(RecordDtoAvnResidentiAnag r,
                                   InformazioniAnagrafiche informazioniAnagrafiche,
                                   ObjectFactory objAnag) {

        //ASSISTITO
        InformazioniAnagrafiche.Assistito currentAssistito = creaAssistito(r, objAnag);
        informazioniAnagrafiche.getAssistito().add(currentAssistito);
    }

    private InformazioniAnagrafiche.Assistito creaAssistito(RecordDtoAvnResidentiAnag r,
                                                            ObjectFactory objAnag) {

        InformazioniAnagrafiche.Assistito assistito = objAnag.createInformazioniAnagraficheAssistito();
        assistito.setTipoTrasmissione(r.getTipoTrasmissioneAnagrafica());
        assistito.setIdAssistito(r.getIdAssistito());
        assistito.setValiditaCI(BigInteger.valueOf(r.getValiditaCI()));
        assistito.setTipologiaCI(BigInteger.valueOf(r.getTipologiaCI()));
        assistito.setSesso(r.getSesso());
        XMLGregorianCalendar dataTrasferimentoResidenza = null;
        XMLGregorianCalendar dataNascita = null;
        XMLGregorianCalendar dataDecesso = null;
        try {
            dataTrasferimentoResidenza = r.getDataTrasferimentoResidenza() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataTrasferimentoResidenza()) : null;
            dataNascita = r.getDataNascita() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataNascita()) : null;
            dataDecesso = r.getDataDecesso() != null ? DatatypeFactory.newInstance().newXMLGregorianCalendar(r.getDataDecesso()) : null;
        } catch (DatatypeConfigurationException e) {
            log.debug(e.getMessage(), e);
        }
        assistito.setDataNascita(dataNascita);
        assistito.setComuneResidenza(r.getCodiceComuneResidenza());
        assistito.setAslResidenza(r.getCodiceAslResidenza());
        assistito.setRegioneResidenza(r.getCodiceRegioneResidenza());
        assistito.setStatoEsteroResidenza(r.getStatoEsteroResidenza());
        assistito.setDataTrasferimentoResidenza(dataTrasferimentoResidenza);
        assistito.setComuneDomicilio(r.getCodiceComuneDomicilio());
        assistito.setAslDomicilio(r.getCodiceAslDomicilio());
        assistito.setRegioneDomicilio(r.getCodiceRegioneDomicilio());
        assistito.setCittadinanza(r.getCittadinanza());
        assistito.setDataDecesso(dataDecesso);
        return assistito;
    }

    public InformazioniAnagrafiche creaInformazioniAnagrafiche(List<RecordDtoAvnResidentiAnag> records, InformazioniAnagrafiche informazioniAnagrafiche) {

        //Imposto gli attribute element
        String modalita = records.get(0).getModalita();
        String codiceRegione = records.get(0).getCodRegione();

        if (informazioniAnagrafiche == null) {
            ObjectFactory objAnagRes = new ObjectFactory();
            informazioniAnagrafiche = objAnagRes.createInformazioniAnagrafiche();
            informazioniAnagrafiche.setCodiceRegione(codiceRegione);
            informazioniAnagrafiche.setModalita(Modalita.fromValue(modalita));

            for (RecordDtoAvnResidentiAnag r : records) {
                if (!r.getTipoTrasmissioneAnagrafica().equalsIgnoreCase("NM")) {
                    creaAnagraficaXml(r, informazioniAnagrafiche, objAnagRes);
                }
            }
        }
        return informazioniAnagrafiche;
    }

}
