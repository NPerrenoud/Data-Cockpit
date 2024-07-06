package ch.epfl.datacockpit.visualizer.global_gui;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.epfl.general_libraries.results.AbstractResultsDisplayer;
import ch.epfl.general_libraries.results.AdvancedDataRetriever;
import ch.epfl.general_libraries.results.ResultDisplayService;
import ch.epfl.general_libraries.utils.DateAndTimeFormatter;
import ch.epfl.datacockpit.visualizer.display.AbstractChartProvider;
import ch.epfl.datacockpit.visualizer.display.AbstractChartProvider.AbstractChartPanel;
import ch.epfl.datacockpit.visualizer.display.panels.BarChartPanel;
import ch.epfl.datacockpit.visualizer.display.panels.HistChartPanel;
import ch.epfl.datacockpit.visualizer.display.panels.ParetoPanel;
import ch.epfl.datacockpit.visualizer.display.panels.XYLineChartPanel;



public class DefaultResultDisplayingGUI implements ResultDisplayService {

	private AdvancedDataRetriever retriever;

	JFrame frame = null;

	private List<Class<? extends AbstractChartPanel>> displayerClasses;
	private ComplexDisplayPanel cmp;

	// Allows Tree service loader to work properly and leverage Service Provider Interface (SPI) pattern
	public DefaultResultDisplayingGUI() {
		super();
		this.displayerClasses = getDefaultDisplayers();
	}

	@Override
	public void displayResults(AdvancedDataRetriever retriever) {
		this.displayResults(retriever, "Data-viz", null, null, null, new String[]{});
	}

	@Override
	public void displayResults(AdvancedDataRetriever retriever, String title, String yAxis, String xAxis, String shape, String[] colors) {
		this.retriever = retriever;
		frame = new JFrame();
		cmp = new ComplexDisplayPanel(this.retriever, this.displayerClasses, title, frame);
		if (yAxis != null && xAxis != null && shape != null && colors != null) {
			cmp.setDefault(yAxis, xAxis, shape, colors);
		}
		frame.setTitle(title + " - " + DateAndTimeFormatter.getDateAndTime(System.currentTimeMillis()));
		frame.setJMenuBar(cmp.getMenubar());
		frame.setContentPane(cmp);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(new java.awt.Dimension(1000, 750));
		frame.setVisible(true);
		//	frame.setExtendedState(frame.MAXIMIZED_BOTH);

	}

	private static List<Class<? extends AbstractChartPanel>> getDefaultDisplayers() {
		List<Class<? extends AbstractChartProvider.AbstractChartPanel>> list = new ArrayList<Class<? extends AbstractChartProvider.AbstractChartPanel>>();
		list.add(XYLineChartPanel.class);
		list.add(BarChartPanel.class);
		list.add(HistChartPanel.class);
		list.add(ParetoPanel.class);
		return list;
	}
}
