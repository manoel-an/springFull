package com.algaworks.brewer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.report.ReportLoader;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

@SuppressWarnings("deprecation")
@Service
public class RelatorioService {
	
	private Map<String, Object> parametros;
	
	public void gerarPDF(String jasper, List<?> objetos, String nomeRelatorio, Boolean imprimir, HttpServletResponse response) throws Exception{
		try {
			InputStream jasperStream = ReportLoader.class.getResourceAsStream(jasper + ".jasper");
			JRBeanCollectionDataSource jrds = new JRBeanCollectionDataSource(objetos);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, getParametros(), objetos == null ? new JREmptyDataSource() : jrds);
			OutputStream outStream = response.getOutputStream();
			response.setContentType(imprimir ? "application/pdf" : "application/x-pdf");
		    response.setHeader("Content-disposition", "inline; filename=" + nomeRelatorio + ".pdf");
		    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} catch (JRException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		}finally {
			getParametros().clear();
		}
	}
	
	public void gerarDOC(String jasper, List<?> objetos, String nomeRelatorio, HttpServletResponse response) throws Exception{
		JRBeanCollectionDataSource jrds = new JRBeanCollectionDataSource(objetos);
        File arquivoIReport = new File(ReportLoader.class.getResource("").getPath() + jasper + ".jasper");
        JasperReport jasperReport = null;
        JasperPrint printer = null;
        File rtfFile = null;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
            printer = JasperFillManager.fillReport(jasperReport, getParametros(),
                    (objetos == null ? new JREmptyDataSource() : jrds));
            rtfFile = new File(ReportLoader.class.getResource("").getPath() + nomeRelatorio + new Date().getTime());
            JRRtfExporter jrRtfExporter = new JRRtfExporter();
            jrRtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, printer);
            jrRtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE, rtfFile);
            jrRtfExporter.exportReport();
            
            FileInputStream stream = new FileInputStream(rtfFile);
            byte[] b = IOUtils.toByteArray(stream);
            
            response.setContentType("application/rtf");  
            response.setHeader("Content-disposition", "inline;filename=" + nomeRelatorio + ".doc");  
            response.getOutputStream().write(b);  
            response.getCharacterEncoding();  
            
            stream.close();
        } catch (JRException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		} finally {
			if(rtfFile.exists())
            	rtfFile.delete();
			getParametros().clear();
		}
	}
	
	public void gerarXLS(String jasper, List<?> objetos, String nomeRelatorio, HttpServletResponse response) throws Exception{
		File excelFile = null;
		try {
			JRDataSource jrds = new JRBeanArrayDataSource((objetos != null ? objetos.toArray() : null));
	        File arquivoIReport = new File(ReportLoader.class.getResource("").getPath() + jasper + ".jasper");
	        JasperReport jasperReport = null;
	        JasperPrint printer = null;
            jasperReport = (JasperReport) JRLoader.loadObject(arquivoIReport);
            printer = JasperFillManager.fillReport(jasperReport, getParametros(),
                    (objetos == null ? new JREmptyDataSource() : jrds));
            excelFile = new File(ReportLoader.class.getResource("").getPath() + nomeRelatorio + new Date().getTime());
            JRXlsExporter jrxlsExporter = new JRXlsExporter();
            jrxlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, printer);
            jrxlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE, excelFile);
            jrxlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            jrxlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
            jrxlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            jrxlsExporter.exportReport();
            
            FileInputStream stream = new FileInputStream(excelFile);
            byte[] b = IOUtils.toByteArray(stream);
            
            response.setContentType("application/xls");  
            response.setHeader("Content-disposition", "inline;filename=" + nomeRelatorio + ".xls");  
            response.getOutputStream().write(b);  
            response.getCharacterEncoding();  
            
            stream.close();
        } catch (JRException e) {
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			throw e;
		} finally {
			if(excelFile.exists())
            	excelFile.delete();
			getParametros().clear();
		}
	}

	public void adicionarParametro(String chave, Object valor){
		this.getParametros().put(chave, valor);
	}

	public Map<String, Object> getParametros() {
		if(parametros == null){
			parametros = new HashMap<>();
		}
		return parametros;
	}
}
