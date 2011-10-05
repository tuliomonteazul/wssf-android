package br.unifor.wssf.core;
import java.util.Timer;


public class WSSFGraphicMonitor implements WSSFInvocationListener{

   private static final long serialVersionUID = 1L;
//   private final Graph graph = new InteractiveGraph();
//   private final Map<WSSFInvocationThread, Function> functions =  new HashMap<WSSFInvocationThread, Function>();
   private final Timer timer = new Timer("Timer");
   private int order = 1;

	public	WSSFGraphicMonitor(String subtitle) {
//		initGraph();
//		initTimer();
//		getContentPane().add(graph);
//		setTitle("WSSFInvocationThreadMonitor - Qtde Leituras x Taxa" + " - " + subtitle);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setSize(640, 480);
//		validate();
//		setVisible(true);
	}
	
//	public void addWSSFInvocationThread(WSSFInvocationThread i) {
//		ChartStyle style = new ChartStyle();
//		style.setPaint(createColor());
//		functions.put(i, new Function(i.getHostName()+":"+i.getHostPort()));
//		graph.addFunction(functions.get(i),style);
//	}
//	
//	private void initGraph() {
//		graph.getXAxis().setZigZaginess(BigDecimal.valueOf(7L, 1));
//		graph.getYAxis().setZigZaginess(BigDecimal.valueOf(7L, 1));
//		graph.setBackground(Color.WHITE);
//	}
//   
//	private void initTimer() {
//		timer.schedule(new TimerTask() {
//
//			public void run() {
//				graph.render();
//				graph.repaint();
//			}
//
//		}, 200L, 1000L);
//	}
//	
//	private Color createColor(){
//		
//	  switch (order++) {
//		case 1: return Color.blue;
//		case 2: return Color.green;
//		case 3: return Color.yellow;
//		case 4: return Color.black;
//		case 5: return Color.red;	
//		default:
//			int red=(int)(Math.random()*255);
//			int green=(int)(Math.random()*255);
//			int blue=(int)(Math.random()*255);
//			return new Color(red, green, blue);
//		}
//		
//	}
//	
//	private Color createColor(WSSFInvocationThread i){
//		
//		if (i.getHostName().indexOf("isc.org") != -1){
//			return Color.green;
//		}else if (i.getHostName().indexOf("3347.voxcdn") != -1){
//			return Color.BLUE;
//		}else if (i.getHostName().indexOf("easynews") != -1){
//			return Color.yellow;
//		}else if (i.getHostName().indexOf(".br") != -1){
//			return Color.GREEN;
//		}else if (i.getHostName().indexOf(".voxcdn.com") != -1){
//			return Color.BLACK;
//		}else if (i.getHostName().indexOf(".za") != -1){
//			return Color.RED;
//		}else if (i.getHostName().indexOf(".jp") != -1){
//			return Color.YELLOW;
//		}else if (i.getHostName().indexOf(".de") != -1 ){
//			return Color.RED;
//		}else{
//			int red=(int)(Math.random()*255);
//			int green=(int)(Math.random()*255);
//			int blue=(int)(Math.random()*255);
//			
//			return new Color(red, green, blue);
//		}
//
//	}
	
	
	public void serverConnectionOpened(WSSFInvocationThread i) {
		//ChartStyle style = new ChartStyle();
		//style.setPaint(createColor(i));
		//functions.put(i, new Function(i.getHostName()+":"+i.getHostPort()));
		//graph.addFunction(functions.get(i),style);
	}

	int qtdLog = 0;
	long leituras = 0;
	double size = 1091716.0;
	public void serverDataReceived(WSSFInvocationThread i,
			int qtBytesReaded) {
		qtdLog++;
		leituras++;
		if(qtdLog == 1){
		   //long time = System.currentTimeMillis() - i.getConnectionTime();
		  //functions.get(i).addPoint(new BigDecimal(i.getBytesReceived()/size * 100), new BigDecimal(i.getTransferRate()));
//		  functions.get(i).addPoint(new BigDecimal(leituras), new BigDecimal(i.getTransferRate()));
		   qtdLog = 0;
		}
	}

	public void serverResponseReceived(WSSFInvocationThread invocationThread,
			byte[] resp) {}

	public void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {}

	public void serverRequestCanceled(WSSFInvocationThread invocationThread) {
		
	}
}


