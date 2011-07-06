package de.zib.gndms.model.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

/**
 * The slice configuration checks and accesses a ConfigHolder for a slice, which
 * has to consist (at least) of the following fields: <br>
 * directory - the (relative) path of the slice as text<br>
 * owner - the owner of the slice as text<br>
 * termination - the termination time of the slice as number representing
 * standard base time.
 * 
 * @author Ulrike Golas
 * 
 */

public class SliceConfiguration extends ConfigHolder {
	/**
	 * The key for the slice's directory.
	 */
	public static final String DIRECTORY = "directory";
	/**
	 * The key for the slice's owner.
	 */
	public static final String OWNER = "owner";
	/**
	 * The key for the slice's termination time.
	 */
	public static final String TERMINATION = "termination";

	/**
	 * Checks if a given config holder is a valid slice configuration.
	 * 
	 * @param config
	 *            The config holder.
	 * @return true, if it is a valid slice configuration; otherwise false.
	 */
	public static boolean checkSliceConfiguration(final ConfigHolder config) {
		JsonNode node = config.getNode();
		try {
			if (!node.findValue(DIRECTORY).isTextual()
					|| !node.findValue(OWNER).isTextual()
					|| !node.findValue(TERMINATION).isNumber()) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Constructs the slice configuration of a given slice.
	 * 
	 * @param slice
	 *            The slice.
	 * @return The config holder.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	public static ConfigHolder getSliceConfiguration(final Slice slice)
			throws IOException, UpdateRejectedException {
		String directory = slice.getDirectoryId();
		String owner = slice.getOwner();
		Long termination = slice.getTerminationTime().getTimeInMillis();
		
		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		JsonNode dn = ConfigHolder.parseSingle(factory, createSingleEntry(DIRECTORY, directory));
		JsonNode on = ConfigHolder.parseSingle(factory, createSingleEntry(OWNER, owner));
		JsonNode tn = ConfigHolder.parseSingle(factory, createSingleEntry(TERMINATION, termination));
		config.update(editor, dn);
		config.update(editor, on);
		config.update(editor, tn);

		return config;
	}

	/**
	 * Returns the directory of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The directory.
	 */
	public static String getDirectory(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(DIRECTORY).isTextual()) {
				return config.getNode().findValue(DIRECTORY).getTextValue();
			} else {
				throw new WrongConfigurationException("The key " + DIRECTORY + " exists but is no text value.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + DIRECTORY + " does not exist.");
		}
	}

	/**
	 * Returns the owner of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The owner.
	 */
	public static String getOwner(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(OWNER).isTextual()) {
				return config.getNode().findValue(OWNER).getTextValue();
			} else {
				throw new WrongConfigurationException("The key " + OWNER + " exists but is no text value.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + OWNER + " does not exist.");
		}
	}

	/**
	 * Returns the termination time of a slice configuration.
	 * 
	 * @param config
	 *            The config holder, which has to be a valid slice
	 *            configuration.
	 * @return The directory.
	 */
	public static Calendar getTerminationTime(final ConfigHolder config) {
		try {
			if (config.getNode().findValue(TERMINATION).isNumber()) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTimeInMillis(config.getNode().findValue(TERMINATION).getLongValue());
				return cal;
			} else {
				throw new WrongConfigurationException("The key " + TERMINATION + " exists but is no number.");
			}
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("The key " + TERMINATION + " does not exist.");
		}
	}
}
