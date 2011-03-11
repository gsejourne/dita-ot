/*
 * This file is part of the DITA Open Toolkit project hosted on
 * Sourceforge.net. See the accompanying license.txt file for 
 * applicable licenses.
 */

/*
 * (c) Copyright IBM Corp. 2004, 2005 All Rights Reserved.
 */
package org.dita.dost.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dita.dost.exception.DITAOTException;
import org.dita.dost.log.DITAOTLogger;
import org.dita.dost.log.MessageUtils;
import org.dita.dost.pipeline.AbstractPipelineInput;
import org.dita.dost.pipeline.AbstractPipelineOutput;
import org.dita.dost.reader.MergeMapParser;
import org.dita.dost.util.Constants;

/** 
 * The module handles topic merge in issues as PDF. 
 */
final class TopicMergeModule implements AbstractPipelineModule {
	
    private DITAOTLogger logger;
    
	/**
	 * Default Constructor.
	 *
	 */
	public TopicMergeModule() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setLogger(final DITAOTLogger logger) {
        this.logger = logger;
    }
	
	/**
	 * Entry point of TopicMergeModule.
	 * @see org.dita.dost.module.AbstractPipelineModule#execute(org.dita.dost.pipeline.AbstractPipelineInput)
	 * @param input Input parameters and resources.
	 * @return null
	 * @throws DITAOTException exception
	 */
	public AbstractPipelineOutput execute(final AbstractPipelineInput input)
			throws DITAOTException {
	    if (logger == null) {
            throw new IllegalStateException("Logger not set");
        }
		final String ditaInput = input
		.getAttribute(Constants.ANT_INVOKER_PARAM_INPUTMAP);
		final String style = input
		.getAttribute(Constants.ANT_INVOKER_EXT_PARAM_STYLE);
		final String out = input
		.getAttribute(Constants.ANT_INVOKER_EXT_PARAM_OUTPUT);
		final String tempdir = input
		.getAttribute(Constants.ANT_INVOKER_PARAM_TEMPDIR);
		final MergeMapParser mapParser = new MergeMapParser();
		
		if (ditaInput == null || !new File(ditaInput).exists()){
			logger.logError(MessageUtils.getMessage("DOTJ025E").toString());
			return null;
		}
		
		if ( out == null ){
			logger.logError(MessageUtils.getMessage("DOTJ026E").toString());
			return null;
		}
		
		

		mapParser.read(ditaInput+"|"+tempdir);
		final String midResult = new StringBuffer(Constants.XML_HEAD).append(
				"<dita-merge xmlns:ditaarch=\"http://dita.oasis-open.org/architecture/2005/\">")
			.append(((StringBuffer)mapParser.getContent().getValue())).append("</dita-merge>").toString();
		final StringReader midStream = new StringReader(midResult);
		
		OutputStreamWriter output = null;
		try{
		    final File outputDir = new File(out).getParentFile();
			if (!outputDir.exists()){
				outputDir.mkdirs();
			}
			if (style != null){
				final TransformerFactory factory = TransformerFactory.newInstance();
				final File styleFile = new File(style);
				final Transformer transformer = factory.newTransformer(new StreamSource(styleFile.toURI().toString()));
				transformer.transform(new StreamSource(midStream), new StreamResult(new FileOutputStream(new File(out))));
			}else{
				output = new OutputStreamWriter(new FileOutputStream(out),Constants.UTF8);
				output.write(midResult);
				output.flush();
			}
		}catch (final Exception e){
			//use java logger to log the exception
			logger.logException(e);
		}finally{
			try{
				if (output !=null){
					output.close();
				}
				midStream.close();
			}catch (final Exception e){
				//use java logger to log the exception
				logger.logException(e);
			}
		}
		
		return null;
	}

}
