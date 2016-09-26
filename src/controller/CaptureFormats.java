/**
dsj demo code.
You may use, modify and redistribute this code under the terms laid out in the header of the DSJDemo application.
copyright 2005-7
N.Peters
humatic GmbH
Berlin, Germany
**/

package controller;

import de.humatic.dsj.DSCapture;
import de.humatic.dsj.DSFilterInfo;
import de.humatic.dsj.DSFiltergraph;
import de.humatic.dsj.DSJUtils;
import de.humatic.dsj.DSMediaType;


public class CaptureFormats implements java.beans.PropertyChangeListener {

	private DSCapture graph;

	javax.swing.JFrame f,
					   imageFrame;

	/** video device index **/

	int DEV_INDEX = 1;

	public CaptureFormats() {}

	public void createGraph() {

		f = new javax.swing.JFrame("dsj - change capture formats");

		/** Need to call queryDevices(1) to get full information on devices **/

		DSFilterInfo[][] dsi = DSCapture.queryDevices(1);

		/** if desired, set initial format **/

		//dsi[0][DEV_INDEX].setPreferredFormat(2);

		graph = new DSCapture(DSFiltergraph.DD7, dsi[0][DEV_INDEX], false, DSFilterInfo.doNotRender(), this);

		f.add(java.awt.BorderLayout.CENTER, graph.asComponent());

		final javax.swing.JComboBox formats = new javax.swing.JComboBox();

		formats.setLightWeightPopupEnabled(false);

		DSMediaType[] mf = dsi[0][DEV_INDEX].getDownstreamPins()[0].getFormats();

		for (int i = 0; i < mf.length; i++) {

			formats.addItem(mf[i].getDisplayString());

		}

		formats.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent e) {

				graph.getActiveVideoDevice().setOutputFormat(formats.getSelectedIndex());

			}

		});

		f.add(java.awt.BorderLayout.NORTH, formats);

		final javax.swing.JButton fd = new javax.swing.JButton("WDM dialog");

		fd.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent e) {

				graph.getActiveVideoDevice().showDialog(DSCapture.CaptureDevice.WDM_CAPTURE);

			}

		});

		f.add(java.awt.BorderLayout.WEST, fd);

		final javax.swing.JButton gi = new javax.swing.JButton("grab frame");

		gi.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent e) {

				java.awt.image.BufferedImage bi = graph.getImage();

				try{ imageFrame.dispose(); } catch (NullPointerException ne){}

				imageFrame = new javax.swing.JFrame("dsj captured frame");

				imageFrame.add(java.awt.BorderLayout.CENTER, new javax.swing.JLabel(new javax.swing.ImageIcon(bi)));

				javax.swing.JTextArea ta = new javax.swing.JTextArea(bi.toString(), 5, 1);

				ta.setLineWrap(true);

				imageFrame.add(java.awt.BorderLayout.SOUTH, ta);

				imageFrame.setPreferredSize(new java.awt.Dimension(bi.getWidth()+100, bi.getHeight()+200));

				imageFrame.pack();

				imageFrame.setLocation(600, 200);

				imageFrame.setVisible(true);

			}

		});

		f.add(java.awt.BorderLayout.EAST, gi);

		f.pack();

		f.setVisible(true);

		/**
		Don't do this at home. This demo relies on dsj closing and disposing off filtergraphs when the JVM exits. This is
		OK for a "open graph, do something & exit" style demo, but real world applications should take care of calling
		dispose() on filtergraphs they're done with themselves.
		**/

		f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	}

	public void propertyChange(java.beans.PropertyChangeEvent pe) {

		if (DSJUtils.getEventType(pe) == DSFiltergraph.FORMAT_CHANGED) {

			f.add("Center", graph.asComponent());

			f.pack();

		}


	}

	public static void main(String[] args){

		new CaptureFormats().createGraph();

	}


}