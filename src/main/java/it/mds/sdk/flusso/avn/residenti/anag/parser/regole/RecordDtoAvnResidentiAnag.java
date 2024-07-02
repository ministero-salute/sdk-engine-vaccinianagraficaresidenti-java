/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag.parser.regole;

import com.opencsv.bean.CsvBindByPosition;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDtoAvnResidentiAnag extends RecordDtoGenerico {

    //ID_ASS~MOD_INV~COD_REG~VLD_COD_ID~TIP_COD_ID~COD_SEX~DAT_NSC~COD_CMN_RES~COD_ASL_RES~COD_REG_RES~COD_STT_RES~DAT_TRA_RES~COD_CMN_DOM~COD_ASL_DOM~COD_REG_DOM~CITT~DAT_DEC~TIP_TRASM_ANAG

    @CsvBindByPosition(position = 0)
    private String idAssistito;
    @CsvBindByPosition(position = 1)
    private String modalita;
    @CsvBindByPosition(position = 2)
    private String codRegione;
    @CsvBindByPosition(position = 3)
    private Integer validitaCI;
    @CsvBindByPosition(position = 4) //TIP_COD_ID
    private Integer tipologiaCI;
    @CsvBindByPosition(position = 5) //COD_SEX
    private String sesso;
    @CsvBindByPosition(position = 6) //DAT_NSC
    private String dataNascita;
    @CsvBindByPosition(position = 7) //COD_CMN_RES
    private String codiceComuneResidenza;
    @CsvBindByPosition(position = 8) //COD_ASL_RES
    private String codiceAslResidenza;
    @CsvBindByPosition(position = 9) //COD_REG_RES
    private String codiceRegioneResidenza;
    @CsvBindByPosition(position = 10) //COD_STT_RES
    private String statoEsteroResidenza;
    @CsvBindByPosition(position = 11) //DAT_TRA_RES
    private String dataTrasferimentoResidenza;
    @CsvBindByPosition(position = 12) //COD_CMN_DOM
    private String codiceComuneDomicilio;
    @CsvBindByPosition(position = 13) //COD_ASL_DOM
    private String codiceAslDomicilio;
    @CsvBindByPosition(position = 14) //COD_REG_DOM
    private String codiceRegioneDomicilio;
    @CsvBindByPosition(position = 15) //CITT
    private String cittadinanza;
    @CsvBindByPosition(position = 16) //DAT_DEC
    private String dataDecesso;
    @CsvBindByPosition(position = 17)
    private String tipoTrasmissioneAnagrafica;

}
