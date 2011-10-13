package br.unifor.wssf.experiment.dao;


public class ExcelExperimentDAO {
//public class ExcelExperimentDAO implements ExperimentDAO {
//	private HSSFWorkbook workbook;
//	public ExcelExperimentDAO() throws FileNotFoundException, IOException {
//		File file = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + "/experiments.xls");
//		if (!file.isFile()) {
//			file.createNewFile();
//		}
//		POIFSFileSystem fs = new POIFSFileSystem( new FileInputStream(file));
//		workbook = new HSSFWorkbook(fs);
//	}
//	
//    public List<Experiment> getAllExperiments() {
//		return null;
//	}
//
//	public Experiment getExperiment(String id) {
//		return null;
//	}
//
//	public void insertExperiment(Experiment e) {
//      
//		insertExperimentOnSheet(e, "ALL");	// Insere dados do experimento na planilha geral
//		String policyNameId = e.getId().substring(e.getId().indexOf(".")+1, e.getId().lastIndexOf("."));    //split("\\.")[1];		
//		if (policyNameId.equals("NO")){
//			String replicaId = e.getId().split("\\.")[0];
//			insertExperimentOnSheet(e, replicaId); // Insere dados do experimento na planilha referente � r�plica
//		} else {
//			
//			if (policyNameId.startsWith("BP")){ //nome da planilha n�o pode ter caracteres especiais
//				
//				policyNameId = policyNameId.replaceAll("\\[", " ");
//				policyNameId = policyNameId.replaceAll("\\]", " ");
//				policyNameId = policyNameId.replaceAll("\\,", " ");
//				policyNameId = policyNameId.replaceAll("\\.", "");
//			}
//			
//			insertExperimentOnSheet(e, policyNameId); // Insere dados do experimento na planilha referente � pol�tica
//		}
//		
//      
//	}
//	
//
//	public void commit() throws IOException {
//		
//		FileOutputStream fs = new FileOutputStream(TextFileReplicaDAO.REPLICA_FILE_PATH + "/experiments.xls");
//        workbook.write(fs);
//		
//	}
//	
//	/**
//	 * Insere uma nova linha contendo os dados do experimento na planilha de nome informado por par�metro.
//	 * Cria planilha caso n�o exista.
//	 * 
//	 * @param e - dados do experimento
//	 * @param s - planilha
//	 */
//	private void insertExperimentOnSheet(Experiment e, String sheetName){
//		
//		HSSFSheet s = workbook.getSheet(sheetName);
//	    
//		if (s == null) {
//			s = workbook.cloneSheet(0);
//			workbook.setSheetName(workbook.getSheetIndex(s.getSheetName()), sheetName);
//		}
//		
//		int i = 0;
//	      HSSFRow row = s.getRow(i);
//	      while (row != null){
//	    	  i++;
//	    	  row = s.getRow(i);
//	      }
//	      row = s.createRow((short)i);
//	      row.createCell(0).setCellValue(e.getId()==null?"-":e.getId());
//	      row.createCell(1).setCellValue(e.getTime()==null?new Date(0):e.getTime());
//	      row.createCell(2).setCellValue(e.getRequestedURL()==null?"-":e.getRequestedURL());
//	      row.createCell(3).setCellValue(e.getPolicyName()==null?"-":e.getPolicyName());
//	      row.createCell(4).setCellValue(e.getDataReceived()==null?0:e.getDataReceived());
//	      row.createCell(5).setCellValue(e.getElapsedTime()==null?0:e.getElapsedTime());
//	      row.createCell(6).setCellValue(e.getRequestStatus()==null?"-":e.getRequestStatus());
//	      row.createCell(7).setCellValue(e.getSelectedServer()==null?"-":e.getSelectedServer());
//	      row.createCell(8).setCellValue(e.getFirstConnectionTime()==null?0:e.getFirstConnectionTime());
//	      row.createCell(9).setCellValue(e.getFirstReadTime()==null?0:e.getFirstReadTime());
//	}

}
