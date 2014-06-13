/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.upload.producer;

import java.io.File;
import java.io.IOException;

import com.timeInc.ark.issue.IssueMeta;
import com.timeInc.ark.packager.PackageInfo;
import com.timeInc.ark.packager.Packager;
import com.timeInc.ark.parser.Parser;

/**
 * A PackageProducer links together a {@link Parser} with the {@link Packager}
 * 
 * @param <T> the package
 * @param <V> the parse information
 */
public abstract class PackageProducer<T extends PackageInfo, V>  {
	private Integer id;

	protected PackageProducer() {}

	/**
	 * Produces a package to be uploaded to a destination
	 * after parsing it.
	 *
	 * @param content the unparsed content
	 * @param outputDirectory the output directory
	 * @param meta the meta
	 * @return the package
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public T produce(File content, File outputDirectory, IssueMeta meta) throws IOException  {
		V parsedMetaData = parse(content, meta);
		return getPackager().pack(parsedMetaData, outputDirectory, meta);
	}

	/**
	 * Requires unpacked.
	 *
	 * @return true, if it does
	 */
	public final boolean requiresUnpacked() {
		return getParser().requiresUnpacked();
	}

	protected abstract V parse(File content, IssueMeta meta);

	/**
	 * Gets the parser.
	 *
	 * @return the parser
	 */
	public abstract Parser<?> getParser();
	
	/**
	 * Gets the packager.
	 *
	 * @return the packager
	 */
	public abstract Packager<T, V> getPackager();
}
