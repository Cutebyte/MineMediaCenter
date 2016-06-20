package net.hopskocz.mmc.launcher.downloader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProgressWindow {

	/**
	 * 
	 */
	private JPanel contentPanel;
	private JProgressBar progressBar;
	private JProgressBar progressBar_1;
	private JLabel lblNewLabel;
	private JDialog dialog;

	/**
	 * Create the dialog.
	 */
	public ProgressWindow(JFrame parent) {

		dialog = new JDialog( parent );

		contentPanel = new JPanel();

		dialog.setBounds( 100, 100, 368, 136 );
		dialog.getContentPane().setLayout( new GridLayout( 0, 1, 0, 0 ) );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		dialog.getContentPane().add( contentPanel );
		contentPanel.setLayout( new GridLayout( 0, 1, 0, 0 ) );

		progressBar = new JProgressBar();
		progressBar.setStringPainted( true );
		contentPanel.add( progressBar );
		
		dialog.setTitle( "Trwa pobieranie" );

		progressBar_1 = new JProgressBar();
		progressBar_1.setStringPainted( true );
		contentPanel.add( progressBar_1 );

		lblNewLabel = new JLabel( "Nazwa pliku" );
		lblNewLabel.setHorizontalAlignment( SwingConstants.CENTER );
		contentPanel.add( lblNewLabel );

		dialog.add( contentPanel );

		dialog.setLocationRelativeTo( parent );

		dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
	}

	public boolean isClosed() {
		return !dialog.isDisplayable();
	}

	public void setVisible( boolean visible ) {
		dialog.setVisible( visible );
	}

	public void setVal( int val ) {
		progressBar_1.setValue( val );
	}
    public void setValMax(int val) { progressBar_1.setMaximum(val); }

	public void setStep(int val) {
		progressBar.setValue(val);
	}
    public void setStepMax(int val) { progressBar.setMaximum(val); }

	public void setName( String val ) {
		lblNewLabel.setText( val );
	}

	public void close() {
		dialog.dispose();
	}

}
