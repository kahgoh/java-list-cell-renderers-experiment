/**
 *  Copyright 2011 Kah Goh
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SkinInfo;

/**
 * Provides a builder for creating the {@link JFrame} that will display
 * {@link JComboBox}s for the different.
 * 
 * @author Kah
 */
public class FrameBuilder {

	private final Map<String, RendererInstaller> installerMap = new LinkedHashMap<String, RendererInstaller>();

	/**
	 * Registers a {@link RendererInstaller}. The built {@link JFrame} will
	 * include a {@link JComboBox} with a renderer that is configured by the
	 * {@link RendererInstaller}.
	 * 
	 * @param name
	 *            the name to display beside the {@link JComboBox} that will be
	 *            configured by the {@link RendererInstaller}
	 * @param installer
	 *            the {@link RendererInstaller} that will configure the {@link JComboBox}
	 */
	public void addRendererInstaller(String name, RendererInstaller installer) {
		installerMap.put(name, installer);
	}

	/**
	 * Builds the window (or {@link JFrame}) based on the configuration.
	 * 
	 * @return the created {@link JFrame}
	 */
	public JFrame build() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contents = frame.getContentPane();
		contents.setLayout(new MigLayout());
		contents.add(new JLabel("Look and feel: "), "sgx label");
		contents.add(lnfComboBox(), "wrap, sgx combo");
		contents.add(new JSeparator(JSeparator.HORIZONTAL),
				"growx 100, pushx, sx 2, wrap");

		for (Map.Entry<String, RendererInstaller> entry : installerMap
				.entrySet()) {
			contents.add(new JLabel(entry.getKey() + ": "), "sgx label");
			JComboBox<Fruit> comboBox = new JComboBox<Fruit>(new Fruit[] {
					new Fruit("Apple"), new Fruit("Banana"),
					new Fruit("Orange") });

			RendererInstaller installer = entry.getValue();
			if (installer != null) {
				installer.installRenderer(comboBox);
			}
			contents.add(comboBox, "wrap, sgx combo");
		}

		frame.pack();
		return frame;
	}

	/**
	 * Creates a {@link JComboBox} for changing the look and feel.
	 * 
	 * @return the created {@link JComboBox}
	 */
	private static JComboBox<?> lnfComboBox() {
		final JComboBox<LnfLoader> skinSelector = new JComboBox<LnfLoader>();

		for (LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
			skinSelector.addItem(standard(lookAndFeel));
		}

		Map<String, SkinInfo> skins = SubstanceLookAndFeel.getAllSkins();
		for (SkinInfo skin : skins.values()) {
			skinSelector.addItem(substance(skin));
		}

		skinSelector.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int index = skinSelector.getSelectedIndex();
				final LnfLoader loader = skinSelector.getItemAt(index);
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						loader.loadLnf();
						Window window = SwingUtilities
								.getWindowAncestor(skinSelector);
						if (window != null) {
							SwingUtilities.updateComponentTreeUI(window);
							window.pack();
						}
					}
				});
			}
		});

		return skinSelector;
	}

	/**
	 * Provides a {@link LnfLoader} for the provided {@link LookAndFeelInfo}.
	 * 
	 * @param lookAndFeel
	 *            the {@link LookAndFeelInfo}
	 * @return the {@link LnfLoader}
	 */
	private static LnfLoader standard(final LookAndFeelInfo lookAndFeel) {
		return new LnfLoader() {

			public void loadLnf() {
				try {
					UIManager.setLookAndFeel(lookAndFeel.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public String toString() {
				return lookAndFeel.getName();
			}
		};
	}

	/**
	 * Provides a {@link LnfLoader} for loading a Substance skin.
	 * 
	 * @param skin
	 *            the Substance skin that will be loaded by the
	 *            {@link LnfLoader}
	 * @return the {@link LnfLoader}
	 */
	private static LnfLoader substance(final SkinInfo skin) {
		return new LnfLoader() {

			public void loadLnf() {
				SubstanceLookAndFeel.setSkin(skin.getClassName());
			}

			public String toString() {
				return skin.getDisplayName();
			}
		};
	}

	/**
	 * Interface for an object that loads a look and feel
	 */
	private static interface LnfLoader {
		/**
		 * Changes the look and feel.
		 */
		public void loadLnf();
	}
}
