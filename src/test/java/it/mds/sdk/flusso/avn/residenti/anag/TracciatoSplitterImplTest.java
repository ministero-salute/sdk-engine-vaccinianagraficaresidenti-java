/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag;

import it.mds.sdk.flusso.avn.residenti.anag.parser.regole.RecordDtoAvnResidentiAnag;
import it.mds.sdk.flusso.avn.residenti.anag.parser.regole.conf.ConfigurazioneFlussoAvnResidentiAnag;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.TracciatoSplitterImpl;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.InformazioniAnagrafiche;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.Modalita;
import it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica.ObjectFactory;
import it.mds.sdk.libreriaregole.dtos.CampiInputBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class TracciatoSplitterImplTest {
    @InjectMocks
    @Spy
    TracciatoSplitterImpl tracciatoSplitter;
    List<RecordDtoAvnResidentiAnag> records = new ArrayList<>();
    RecordDtoAvnResidentiAnag r = new RecordDtoAvnResidentiAnag();
    ObjectFactory objectFactory = Mockito.mock(ObjectFactory.class);
    InformazioniAnagrafiche informazioniAnagrafiche = Mockito.mock(InformazioniAnagrafiche.class);
    ConfigurazioneFlussoAvnResidentiAnag conf = Mockito.mock(ConfigurazioneFlussoAvnResidentiAnag.class);

    ConfigurazioneFlussoAvnResidentiAnag.XmlOutput xmlOutput = Mockito.mock(ConfigurazioneFlussoAvnResidentiAnag.XmlOutput.class);
    InformazioniAnagrafiche.Assistito assistito = Mockito.mock(InformazioniAnagrafiche.Assistito.class);

    @BeforeEach
    void init() {
        initMockedRecord(r);
        records.add(r);

    }

    @Test
    void dividiTracciatoTestOk() {
        when(tracciatoSplitter.getObjectFactory()).thenReturn(objectFactory);
        when(tracciatoSplitter.getConfigurazione()).thenReturn(conf);
        when(objectFactory.createInformazioniAnagrafiche()).thenReturn(informazioniAnagrafiche);
        when(conf.getXmlOutput()).thenReturn(xmlOutput);
        when(xmlOutput.getPercorso()).thenReturn("percorso_");
        tracciatoSplitter.dividiTracciato(
                records,
                "idRun"
        );
    }

    @Test
    void dividiTracciatoTestKo() {
        when(tracciatoSplitter.getObjectFactory()).thenReturn(objectFactory);
        when(tracciatoSplitter.getConfigurazione()).thenReturn(conf);
        when(objectFactory.createInformazioniAnagrafiche()).thenReturn(informazioniAnagrafiche);
        when(conf.getXmlOutput()).thenReturn(null);

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> tracciatoSplitter.dividiTracciato(records, "idRun")
        );
    }

    @Test
    void dividiTracciatoTestOk2() {
        RecordDtoAvnResidentiAnag r = records.get(0);
        r.setTipoTrasmissioneAnagrafica("MV");
        records.clear();
        records.add(r);
        when(tracciatoSplitter.getObjectFactory()).thenReturn(objectFactory);
        when(tracciatoSplitter.getConfigurazione()).thenReturn(conf);
        when(objectFactory.createInformazioniAnagrafiche()).thenReturn(informazioniAnagrafiche);
        when(conf.getXmlOutput()).thenReturn(xmlOutput);
        when(xmlOutput.getPercorso()).thenReturn("percorso_");
        when(objectFactory.createInformazioniAnagraficheAssistito()).thenReturn(assistito);
        tracciatoSplitter.dividiTracciato(
                records,
                "idRun"
        );
    }

    @Test
    void creaInformazioniAnagraficheTestOk() {

        when(tracciatoSplitter.getObjectFactory()).thenReturn(objectFactory);
        when(tracciatoSplitter.getConfigurazione()).thenReturn(conf);
        when(objectFactory.createInformazioniAnagrafiche()).thenReturn(informazioniAnagrafiche);
        when(conf.getXmlOutput()).thenReturn(xmlOutput);
        when(xmlOutput.getPercorso()).thenReturn("percorso_");
        when(objectFactory.createInformazioniAnagraficheAssistito()).thenReturn(assistito);
        tracciatoSplitter.creaInformazioniAnagrafiche(
                records,
                null
        );
    }

    private void initMockedRecord(RecordDtoAvnResidentiAnag r) {
        CampiInputBean campiInputBean = new CampiInputBean();
        campiInputBean.setPeriodoRiferimentoInput("Q1");
        campiInputBean.setAnnoRiferimentoInput("2022");
        r.setModalita("RE");
        r.setCodRegione("080");
        r.setTipoTrasmissioneAnagrafica("NM");
        r.setCampiInput(campiInputBean);
        r.setValiditaCI(2022);
        r.setTipologiaCI(2);
    }
}
