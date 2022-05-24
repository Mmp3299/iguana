/*
 * Copyright (c) 2015, Ali Afroozeh and Anastasia Izmaylova, Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 *
 */

package iguana.utils.collections;

import iguana.utils.collections.hash.IntHash5;

public class IntKey4PlusObject implements Key {
	
	private final int k1;
	private final int k2;
	private final int k3;
	private final int k4;
	private final Object obj;	
	
	private final int hash;

	private IntKey4PlusObject(Object obj, int k1, int k2, int k3, int k4, IntHash5 f) {
		this.k1 = k1;
		this.k2 = k2;
		this.k3 = k3;
		this.k4 = k4;
		this.obj = obj;
		this.hash = f.hash(obj.hashCode(), k1, k2, k3, k4);
	}
	
	public static IntKey4PlusObject from(Object obj, int k1, int k2, int k3, int k4, IntHash5 f) {
		return new IntKey4PlusObject(obj, k1, k2, k3, k4, f);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		
		if (!(other instanceof IntKey4PlusObject)) return false;
		
		IntKey4PlusObject that = (IntKey4PlusObject) other;
		return hash == that.hash 
				&& k1 == that.k1 && k2 == that.k2 
				&& k3 == that.k3 && k4 == that.k4
				&& obj.equals(that.obj);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public int[] components() {
		return new int[] {k1, k2, k3, k4};
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d, %d, %s)", k1, k2, k3, k4, obj);
	}

}
