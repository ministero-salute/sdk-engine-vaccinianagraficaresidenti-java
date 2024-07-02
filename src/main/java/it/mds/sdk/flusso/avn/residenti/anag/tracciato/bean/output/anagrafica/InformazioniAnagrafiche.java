//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.14 alle 04:11:10 PM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica;

import jakarta.xml.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per anonymous complex type.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Assistito" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TipoTrasmissione" type="{}TipoTrasmissione"/&gt;
 *                   &lt;element name="IdAssistito" type="{}IdAssistito"/&gt;
 *                   &lt;element name="ValiditaCI" type="{}ValiditaCI"/&gt;
 *                   &lt;element name="TipologiaCI" type="{}TipologiaCI"/&gt;
 *                   &lt;element name="Sesso" type="{}Sesso"/&gt;
 *                   &lt;element name="DataNascita" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="ComuneResidenza" type="{}ComuneResidenza"/&gt;
 *                   &lt;element name="AslResidenza" type="{}AslResidenza"/&gt;
 *                   &lt;element name="RegioneResidenza" type="{}CodiceRegioneResidenza"/&gt;
 *                   &lt;element name="StatoEsteroResidenza" type="{}StatoEsteroResidenza"/&gt;
 *                   &lt;element name="DataTrasferimentoResidenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="ComuneDomicilio" type="{}ComuneDomicilio" minOccurs="0"/&gt;
 *                   &lt;element name="AslDomicilio" type="{}AslDomicilio" minOccurs="0"/&gt;
 *                   &lt;element name="RegioneDomicilio" type="{}CodiceRegioneDomicilio" minOccurs="0"/&gt;
 *                   &lt;element name="Cittadinanza" type="{}Cittadinanza"/&gt;
 *                   &lt;element name="DataDecesso" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="CodiceRegione" use="required" type="{}CodiceRegione" /&gt;
 *       &lt;attribute name="Modalita" use="required" type="{}Modalita" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "assistito"
})
@XmlRootElement(name = "informazioniAnagrafiche")
public class InformazioniAnagrafiche {

    @XmlElement(name = "Assistito", required = true)
    protected List<InformazioniAnagrafiche.Assistito> assistito;
    @XmlAttribute(name = "CodiceRegione", required = true)
    protected String codiceRegione;
    @XmlAttribute(name = "Modalita", required = true)
    protected Modalita modalita;

    /**
     * Gets the value of the assistito property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the assistito property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssistito().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InformazioniAnagrafiche.Assistito }
     */
    public List<InformazioniAnagrafiche.Assistito> getAssistito() {
        if (assistito == null) {
            assistito = new ArrayList<InformazioniAnagrafiche.Assistito>();
        }
        return this.assistito;
    }

    /**
     * Recupera il valore della proprietà codiceRegione.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCodiceRegione() {
        return codiceRegione;
    }

    /**
     * Imposta il valore della proprietà codiceRegione.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodiceRegione(String value) {
        this.codiceRegione = value;
    }

    /**
     * Recupera il valore della proprietà modalita.
     *
     * @return possible object is
     * {@link Modalita }
     */
    public Modalita getModalita() {
        return modalita;
    }

    /**
     * Imposta il valore della proprietà modalita.
     *
     * @param value allowed object is
     *              {@link Modalita }
     */
    public void setModalita(Modalita value) {
        this.modalita = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     *
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="TipoTrasmissione" type="{}TipoTrasmissione"/&gt;
     *         &lt;element name="IdAssistito" type="{}IdAssistito"/&gt;
     *         &lt;element name="ValiditaCI" type="{}ValiditaCI"/&gt;
     *         &lt;element name="TipologiaCI" type="{}TipologiaCI"/&gt;
     *         &lt;element name="Sesso" type="{}Sesso"/&gt;
     *         &lt;element name="DataNascita" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="ComuneResidenza" type="{}ComuneResidenza"/&gt;
     *         &lt;element name="AslResidenza" type="{}AslResidenza"/&gt;
     *         &lt;element name="RegioneResidenza" type="{}CodiceRegioneResidenza"/&gt;
     *         &lt;element name="StatoEsteroResidenza" type="{}StatoEsteroResidenza"/&gt;
     *         &lt;element name="DataTrasferimentoResidenza" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="ComuneDomicilio" type="{}ComuneDomicilio" minOccurs="0"/&gt;
     *         &lt;element name="AslDomicilio" type="{}AslDomicilio" minOccurs="0"/&gt;
     *         &lt;element name="RegioneDomicilio" type="{}CodiceRegioneDomicilio" minOccurs="0"/&gt;
     *         &lt;element name="Cittadinanza" type="{}Cittadinanza"/&gt;
     *         &lt;element name="DataDecesso" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "tipoTrasmissione",
            "idAssistito",
            "validitaCI",
            "tipologiaCI",
            "sesso",
            "dataNascita",
            "comuneResidenza",
            "aslResidenza",
            "regioneResidenza",
            "statoEsteroResidenza",
            "dataTrasferimentoResidenza",
            "comuneDomicilio",
            "aslDomicilio",
            "regioneDomicilio",
            "cittadinanza",
            "dataDecesso"
    })
    public static class Assistito {

        @XmlElement(name = "TipoTrasmissione", required = true)
        protected String tipoTrasmissione;
        @XmlElement(name = "IdAssistito", required = true)
        protected String idAssistito;
        @XmlElement(name = "ValiditaCI", required = true)
        protected BigInteger validitaCI;
        @XmlElement(name = "TipologiaCI", required = true)
        protected BigInteger tipologiaCI;
        @XmlElement(name = "Sesso", required = true)
        protected String sesso;
        @XmlElement(name = "DataNascita", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataNascita;
        @XmlElement(name = "ComuneResidenza", required = true)
        protected String comuneResidenza;
        @XmlElement(name = "AslResidenza", required = true)
        protected String aslResidenza;
        @XmlElement(name = "RegioneResidenza", required = true)
        protected String regioneResidenza;
        @XmlElement(name = "StatoEsteroResidenza", required = true)
        protected String statoEsteroResidenza;
        @XmlElement(name = "DataTrasferimentoResidenza")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataTrasferimentoResidenza;
        @XmlElement(name = "ComuneDomicilio")
        protected String comuneDomicilio;
        @XmlElement(name = "AslDomicilio")
        protected String aslDomicilio;
        @XmlElement(name = "RegioneDomicilio")
        protected String regioneDomicilio;
        @XmlElement(name = "Cittadinanza", required = true)
        protected String cittadinanza;
        @XmlElement(name = "DataDecesso")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dataDecesso;

        /**
         * Recupera il valore della proprietà tipoTrasmissione.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTipoTrasmissione() {
            return tipoTrasmissione;
        }

        /**
         * Imposta il valore della proprietà tipoTrasmissione.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTipoTrasmissione(String value) {
            this.tipoTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà idAssistito.
         *
         * @return possible object is
         * {@link String }
         */
        public String getIdAssistito() {
            return idAssistito;
        }

        /**
         * Imposta il valore della proprietà idAssistito.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setIdAssistito(String value) {
            this.idAssistito = value;
        }

        /**
         * Recupera il valore della proprietà validitaCI.
         *
         * @return possible object is
         * {@link BigInteger }
         */
        public BigInteger getValiditaCI() {
            return validitaCI;
        }

        /**
         * Imposta il valore della proprietà validitaCI.
         *
         * @param value allowed object is
         *              {@link BigInteger }
         */
        public void setValiditaCI(BigInteger value) {
            this.validitaCI = value;
        }

        /**
         * Recupera il valore della proprietà tipologiaCI.
         *
         * @return possible object is
         * {@link BigInteger }
         */
        public BigInteger getTipologiaCI() {
            return tipologiaCI;
        }

        /**
         * Imposta il valore della proprietà tipologiaCI.
         *
         * @param value allowed object is
         *              {@link BigInteger }
         */
        public void setTipologiaCI(BigInteger value) {
            this.tipologiaCI = value;
        }

        /**
         * Recupera il valore della proprietà sesso.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSesso() {
            return sesso;
        }

        /**
         * Imposta il valore della proprietà sesso.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSesso(String value) {
            this.sesso = value;
        }

        /**
         * Recupera il valore della proprietà dataNascita.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getDataNascita() {
            return dataNascita;
        }

        /**
         * Imposta il valore della proprietà dataNascita.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setDataNascita(XMLGregorianCalendar value) {
            this.dataNascita = value;
        }

        /**
         * Recupera il valore della proprietà comuneResidenza.
         *
         * @return possible object is
         * {@link String }
         */
        public String getComuneResidenza() {
            return comuneResidenza;
        }

        /**
         * Imposta il valore della proprietà comuneResidenza.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setComuneResidenza(String value) {
            this.comuneResidenza = value;
        }

        /**
         * Recupera il valore della proprietà aslResidenza.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAslResidenza() {
            return aslResidenza;
        }

        /**
         * Imposta il valore della proprietà aslResidenza.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAslResidenza(String value) {
            this.aslResidenza = value;
        }

        /**
         * Recupera il valore della proprietà regioneResidenza.
         *
         * @return possible object is
         * {@link String }
         */
        public String getRegioneResidenza() {
            return regioneResidenza;
        }

        /**
         * Imposta il valore della proprietà regioneResidenza.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setRegioneResidenza(String value) {
            this.regioneResidenza = value;
        }

        /**
         * Recupera il valore della proprietà statoEsteroResidenza.
         *
         * @return possible object is
         * {@link String }
         */
        public String getStatoEsteroResidenza() {
            return statoEsteroResidenza;
        }

        /**
         * Imposta il valore della proprietà statoEsteroResidenza.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setStatoEsteroResidenza(String value) {
            this.statoEsteroResidenza = value;
        }

        /**
         * Recupera il valore della proprietà dataTrasferimentoResidenza.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getDataTrasferimentoResidenza() {
            return dataTrasferimentoResidenza;
        }

        /**
         * Imposta il valore della proprietà dataTrasferimentoResidenza.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setDataTrasferimentoResidenza(XMLGregorianCalendar value) {
            this.dataTrasferimentoResidenza = value;
        }

        /**
         * Recupera il valore della proprietà comuneDomicilio.
         *
         * @return possible object is
         * {@link String }
         */
        public String getComuneDomicilio() {
            return comuneDomicilio;
        }

        /**
         * Imposta il valore della proprietà comuneDomicilio.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setComuneDomicilio(String value) {
            this.comuneDomicilio = value;
        }

        /**
         * Recupera il valore della proprietà aslDomicilio.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAslDomicilio() {
            return aslDomicilio;
        }

        /**
         * Imposta il valore della proprietà aslDomicilio.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAslDomicilio(String value) {
            this.aslDomicilio = value;
        }

        /**
         * Recupera il valore della proprietà regioneDomicilio.
         *
         * @return possible object is
         * {@link String }
         */
        public String getRegioneDomicilio() {
            return regioneDomicilio;
        }

        /**
         * Imposta il valore della proprietà regioneDomicilio.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setRegioneDomicilio(String value) {
            this.regioneDomicilio = value;
        }

        /**
         * Recupera il valore della proprietà cittadinanza.
         *
         * @return possible object is
         * {@link String }
         */
        public String getCittadinanza() {
            return cittadinanza;
        }

        /**
         * Imposta il valore della proprietà cittadinanza.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setCittadinanza(String value) {
            this.cittadinanza = value;
        }

        /**
         * Recupera il valore della proprietà dataDecesso.
         *
         * @return possible object is
         * {@link XMLGregorianCalendar }
         */
        public XMLGregorianCalendar getDataDecesso() {
            return dataDecesso;
        }

        /**
         * Imposta il valore della proprietà dataDecesso.
         *
         * @param value allowed object is
         *              {@link XMLGregorianCalendar }
         */
        public void setDataDecesso(XMLGregorianCalendar value) {
            this.dataDecesso = value;
        }

    }

}
