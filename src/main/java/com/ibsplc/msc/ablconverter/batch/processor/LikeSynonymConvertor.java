package com.ibsplc.msc.ablconverter.batch.processor;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibsplc.msc.ablconverter.array.data.TableFieldHolder;
import com.ibsplc.msc.ablconverter.core.TableAndField;

public class LikeSynonymConvertor extends BaseConverter {

	@Autowired
	TableFieldHolder fieldHolder;

	@Override
	protected String convert(String lineToProcess) {

		String replacedLine = lineToProcess;

		ListIterator<TableAndField> itr = fieldHolder.getTableAndField().listIterator();

		if (replacedLine.toUpperCase().indexOf("LIKE") != -1) {
			while (itr.hasNext()) {
				TableAndField tf = (TableAndField) itr.next();
				String strToFind = "(?i)(define|def)(.*?)(variable|var)(.*?)(like)(.*?)(\\b" + tf.getTableName() + "\\s*\\.\\s*" + tf.getFieldName() + "\\b)";
				

				Matcher m = Pattern.compile(strToFind).matcher(lineToProcess);
				while (m.find()) {
					replacedLine = doReplace(lineToProcess, tf);
				}
			}
		}

		return replacedLine;
	}
	
	private String doReplace(String lineToProcess, TableAndField tf) {
		String strToFind = "(?i)(define|def)(.*?)(variable|var)(.*?)(like)(.*?)(\\b" + tf.getTableName() + "\\s*\\.\\s*" + tf.getFieldName() + "\\b)";
		String strToReplace =  "$1$2$3$4  as inte extent " + tf.getFieldArraySize();
		lineToProcess = lineToProcess.replaceAll(strToFind, strToReplace);
		return lineToProcess;
		
	}

}
