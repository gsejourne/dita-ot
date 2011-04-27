/*
 * This file is part of the DITA Open Toolkit project hosted on
 * Sourceforge.net. See the accompanying license.txt file for 
 * applicable licenses.
 */

/*
 * (c) Copyright IBM Corp. 2010 All Rights Reserved.
 */
package org.dita.dost.writer;
import java.io.File;
import org.dita.dost.writer.CHMIndexWriter;
import org.dita.dost.writer.EclipseIndexWriter;
import org.dita.dost.writer.IDitaTranstypeIndexWriter;
import org.dita.dost.writer.JavaHelpIndexWriter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestIDitaTranstypeIndexWriter {
	public static IDitaTranstypeIndexWriter idita1= new CHMIndexWriter(); 
	public static IDitaTranstypeIndexWriter idita2= new EclipseIndexWriter();
	public static IDitaTranstypeIndexWriter idita3= new JavaHelpIndexWriter();
	@Test
	public void testiditatranstypeindexwriter()
	{
		String rootpath=System.getProperty("user.dir");
		String path=rootpath + File.separator + "test-stub" + File.separator + "index.xml";
		String outputfilename="test-stub" + File.separator + "iditatranstypewriter";
		assertEquals("test-stub" + File.separator + "iditatranstypewriter.hhk",idita1.getIndexFileName(outputfilename));
		assertEquals(path,idita2.getIndexFileName(outputfilename));
		assertEquals("test-stub" + File.separator + "iditatranstypewriter_index.xml",idita3.getIndexFileName(outputfilename));
	}

}