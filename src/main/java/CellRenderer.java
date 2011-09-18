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
import java.awt.Component;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;

/**
 * Defines the main method that will create and display the dialog of combo
 * boxes.
 * 
 * @author Kah
 */
@SuppressWarnings("serial")
public class CellRenderer {

	public static void main(String args[]) throws InterruptedException,
			InvocationTargetException {
		EventQueue.invokeAndWait(new Runnable() {

			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					
//					SubstanceLookAndFeel.setSkin("org.pushingpixels.substance.api.skin.RavenSkin");
				} catch (Exception e) {
					e.printStackTrace();
				}

				FrameBuilder builder = new FrameBuilder();
				builder.addRendererInstaller("Unchanged", null);
				builder.addRendererInstaller("Basic Renderer Based",
						installer(basicRendererBased()));
				builder.addRendererInstaller("Substance Renderer Based",
						installer(substanceRendererBased()));
				builder.addRendererInstaller("Default Renderer Based",
						installer(defaultRendererBased()));
				builder.addRendererInstaller("Replacing Default", replacingDefault());
				builder.addRendererInstaller("Static Delegate", staticDelegating());
				builder.addRendererInstaller("Replacing Delegating Renderer",
						replacingDelegating());
				builder.build().setVisible(true);
			}
		});
	}

	/**
	 * Creates a {@link ListCellRenderer} that is based on
	 * {@link BasicComboBoxRenderer}.
	 * 
	 * @return the created {@link ListCellRenderer}
	 */
	@SuppressWarnings("unchecked")
	private static ListCellRenderer<? super Fruit> basicRendererBased() {
		return new BasicComboBoxRenderer() {

			@Override
			public Component getListCellRendererComponent(
					@SuppressWarnings("rawtypes") JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {

				return super.getListCellRendererComponent(list, nameOf(value),
						index, isSelected, cellHasFocus);
			}
		};
	}

	/**
	 * Creates a {@link ListCellRenderer} that is based on
	 * {@link DefaultListCellRenderer}.
	 * 
	 * @return the created {@link ListCellRenderer}
	 */
	private static ListCellRenderer<? super Fruit> defaultRendererBased() {

		return new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				return super.getListCellRendererComponent(list, nameOf(value),
						index, isSelected, cellHasFocus);
			}

		};
	}

	/**
	 * Creates a {@link ListCellRenderer} that is based on the default renderer
	 * for the Substance look and feel ({@link SubstanceDefaultListCellRenderer}
	 * ).
	 * 
	 * @return the created {@link ListCellRenderer}
	 */
	private static ListCellRenderer<? super Fruit> substanceRendererBased() {

		return new SubstanceDefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(
					@SuppressWarnings("rawtypes") JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, nameOf(value),
						index, isSelected, cellHasFocus);
			}
		};
	}

	/**
	 * Creates an {@link RendererInstaller} for installing the provided
	 * {@link ListCellRenderer} to a {@link JComboBox}.
	 * 
	 * @param renderer
	 *            the {@link ListCellRenderer} that will be installed by the
	 *            {@link RendererInstaller}
	 * @return the created {@link RendererInstaller}
	 */
	public static RendererInstaller installer(
			final ListCellRenderer<? super Fruit> renderer) {
		return new RendererInstaller() {

			public final void installRenderer(JComboBox<Fruit> comboBox) {
				comboBox.setRenderer(renderer);
			}
		};
	}

	/**
	 * Provides a {@link RendererInstaller} that will install a custom
	 * {@link ListCellRenderer} that delegates back to the
	 * {@link ListCellRenderer} that it replaces.
	 * 
	 * @return the created {@link RendererInstaller}
	 */
	private static RendererInstaller staticDelegating() {
		return new RendererInstaller() {
			public void installRenderer(JComboBox<Fruit> comboBox) {
				final ListCellRenderer<? super Object> original = new JComboBox<Object>()
						.getRenderer();
				comboBox.setRenderer(new ListCellRenderer<Fruit>() {

					public Component getListCellRendererComponent(
							JList<? extends Fruit> list, Fruit value,
							int index, boolean isSelected, boolean cellHasFocus) {

						return original.getListCellRendererComponent(list,
								nameOf(value), index, isSelected, cellHasFocus);
					}
				});
			}
		};
	}

	/**
	 * Creates an {@link RendererInstaller} that installs a
	 * {@link ListCellRenderer} that will delegate to an "unchanged"
	 * {@link ListCellRenderer}. It will also listen for changes to look and
	 * feel and update the renderer that it delegates to.
	 * 
	 * @return the created {@link RendererInstaller}
	 */
	private static RendererInstaller replacingDelegating() {
		return new RendererInstaller() {

			public void installRenderer(JComboBox<Fruit> comboBox) {
				DelegatingRenderer.install(comboBox);
			}
		};
	}

	/**
	 * Creates a {@link RendererInstaller} that will install a
	 * {@link DefaultListCellRenderer} and will replace it with new instances of
	 * {@link DefaultListCellRenderer} when the look and feel changes.
	 * 
	 * @return the created {@link RendererInstaller}
	 */
	private static RendererInstaller replacingDefault() {
		return new RendererInstaller() {

			public void installRenderer(final JComboBox<Fruit> comboBox) {
				comboBox.setRenderer(newDefaultRenderer());
				comboBox.addPropertyChangeListener("UI",
						new PropertyChangeListener() {

							public void propertyChange(PropertyChangeEvent evt) {
								comboBox.setRenderer(newDefaultRenderer());
							}
						});
			}
		};
	}

	/**
	 * Creates an instance of the {@link DefaultListCellRenderer}.
	 * 
	 * @return the created {@link DefaultListCellRenderer}
	 */
	private static DefaultListCellRenderer newDefaultRenderer() {
		return new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				return super.getListCellRendererComponent(list, nameOf(value),
						index, isSelected, cellHasFocus);
			}

		};
	}

	private static String nameOf(Object name) {
		if (name != null && name instanceof Fruit) {
			return ((Fruit) name).getName();
		}

		return "";
	}
}
