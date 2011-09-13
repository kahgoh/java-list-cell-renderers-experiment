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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Provides a custom {@link ListCellRenderer} that updates the delegate when
 * ever the UI of the combo box changes.
 * 
 * @author Kah
 */
public class DelegatingRenderer implements ListCellRenderer<Fruit> {

	/**
	 * Installs a custom renderer for a {@link JComboBox} containing
	 * {@link Fruit} objects. It will display the text returned by
	 * {@link Fruit#getName()}.
	 * 
	 * @param comboBox
	 *            the {@link JComboBox} to install in the renderer in
	 */
	public static void install(JComboBox<Fruit> comboBox) {
		DelegatingRenderer renderer = new DelegatingRenderer(comboBox);
		renderer.initialise();
		comboBox.setRenderer(renderer);
	}

	/**
	 * The instance of the {@link JComboBox} that the renderer is being used in.
	 */
	private final JComboBox<Fruit> comboBox;

	/**
	 * Reference to the renderer that will be given the textual representation
	 * of the value.
	 */
	private ListCellRenderer<? super Object> delegate;

	/**
	 * Private constructor, as it is supposed to be used through
	 * {@link #install(JComboBox)}.
	 * 
	 * @param comboBox
	 *            the instance of the {@link JComboBox} that this renderer is
	 *            being used in
	 */
	private DelegatingRenderer(JComboBox<Fruit> comboBox) {
		this.comboBox = comboBox;
	}

	/**
	 * Called by {@link #install(JComboBox)} to setup the listener to will
	 * update the listener to delegate back to.
	 */
	private void initialise() {
		delegate = new JComboBox<Object>().getRenderer();
		comboBox.addPropertyChangeListener("UI", new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				delegate = new JComboBox<Object>().getRenderer();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getListCellRendererComponent(JList<? extends Fruit> list,
			Fruit value, int index, boolean isSelected, boolean cellHasFocus) {

		return delegate.getListCellRendererComponent(list, value.getName(),
				index, isSelected, cellHasFocus);
	}
}
