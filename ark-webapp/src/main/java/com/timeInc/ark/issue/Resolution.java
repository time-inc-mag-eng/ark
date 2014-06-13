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
package com.timeInc.ark.issue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The Resolution for a folio.
 */
public class Resolution implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int height;
	private int width;
	
	Resolution() {};
	
	/**
	 * Instantiates a new resolution.
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Resolution(File file) throws IOException {
		BufferedImage bimg = ImageIO.read(file);
		int width = bimg.getWidth();
		int height = bimg.getHeight();
		
		this.height = height;
		this.width = width;
	}
	
	/**
	 * Instantiates a new resolution.
	 *
	 * @param height the height
	 * @param width the width
	 */
	public Resolution(int height, int width) {
		this.height = height;
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	
	void setHeight(int height) {
		this.height = height;
	}

	void setWidth(int width) {
		this.width = width;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) { return EqualsBuilder.reflectionEquals(this, obj); }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() { return HashCodeBuilder.reflectionHashCode(this); }	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return width + "x" + height; }	
}
