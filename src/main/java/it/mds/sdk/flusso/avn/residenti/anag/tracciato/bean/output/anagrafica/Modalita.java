//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.06.14 alle 04:11:10 PM CEST 
//


/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag.tracciato.bean.output.anagrafica;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Modalita.
 *
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="Modalita"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;length value="2"/&gt;
 *     &lt;enumeration value="RE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "Modalita")
@XmlEnum
public enum Modalita {

    RE;

    public static Modalita fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }

}
