/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.flusso.avn.residenti.anag.parser.regole;


import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import it.mds.sdk.flusso.avn.residenti.anag.parser.regole.conf.ConfigurazioneFlussoAvnResidentiAnag;
import it.mds.sdk.libreriaregole.dtos.RecordDtoGenerico;
import it.mds.sdk.libreriaregole.parser.ParserTracciato;
import it.mds.sdk.rest.exception.ParseCSVException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component("parserTracciatoAvnResidentiAnag")
public class ParserTracciatoImpl implements ParserTracciato {

    private ConfigurazioneFlussoAvnResidentiAnag conf = new ConfigurazioneFlussoAvnResidentiAnag();

    /**
     * Il metodo converte un File.csv in una List<RecordDtoGenerico> usando come separatore "~"
     *
     * @param tracciato, File.csv di input
     * @return una lista di RecordDtoDir
     */
    @Override
    public List<RecordDtoGenerico> parseTracciato(File tracciato) {
        char separatore = conf.getSeparatore().getSeparatore().charAt(0);
        try (FileReader fileReader = new FileReader(tracciato)) {
            List<RecordDtoGenerico> dirList = new CsvToBeanBuilder<RecordDtoGenerico>(fileReader)
                    .withType(RecordDtoAvnResidentiAnag.class)
                    .withSeparator(separatore)
                    .withSkipLines(1)   //Salta la prima riga del file CSV
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .parse();
            fileReader.close();

            return dirList;

        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new ParseCSVException(ex.getMessage());
        }

        return Collections.emptyList();
    }

    public List<RecordDtoGenerico> parseTracciatoBlocco(File tracciato, int inizio, int fine) {
        StopWatch stopWatch = new StopWatch();
        char separatore = conf.getSeparatore().getSeparatore().charAt(0);
        log.info("Inizio lettura file {} da riga {} a riga {}", tracciato.getName(), inizio, fine);
        stopWatch.start();
        try (Reader reader = Files.newBufferedReader(tracciato.toPath())) {
            List<RecordDtoGenerico> res = new ArrayList<>();
            Iterator<RecordDtoAvnResidentiAnag> it = new CsvToBeanBuilder<RecordDtoAvnResidentiAnag>(reader)
                    .withType(RecordDtoAvnResidentiAnag.class)
                    .withSeparator(separatore)
                    .withSkipLines(inizio + 1)   //Salta la prima riga del file CSV
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build()
                    .iterator();
            int count = inizio;
            int totaleRecordElaborati = 0;
            while (it.hasNext() && count < fine) {
                count++;
                RecordDtoGenerico recordGen = it.next();
                res.add(recordGen);
                totaleRecordElaborati++;
            }
            stopWatch.stop();
            log.info("Fine lettura file {} da riga {} a riga {} con {} record in {} ms", tracciato.getName(), inizio,
                    fine, totaleRecordElaborati, stopWatch.getLastTaskTimeMillis());

            return res;
        } catch (IOException e) {
            throw new ParseCSVException(e.getMessage());
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new ParseCSVException(ex.getMessage());
        }
    }

}
