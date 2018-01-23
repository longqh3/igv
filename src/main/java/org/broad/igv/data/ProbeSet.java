/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2007-2015 Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 * ProbeSet.java
 *
 * Created on October 31, 2007, 5:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.broad.igv.data;

import java.util.*;

/**
 * @author jrobinso
 */
public class ProbeSet {

    private Map<String, List<ExpressionProbe>> probeLists = new HashMap();
    private Map<String, ExpressionProbe> probes = new HashMap();

    public Set<String> getChromosomes() {
        return probeLists.keySet();
    }


    public List<ExpressionProbe> getProbes(String chr) {
        return probeLists.get(chr);
    }

    public ExpressionProbe getProbe(String probeId) {
        return probes.get(probeId);
    }

    public void add(ExpressionProbe probe) {
        probes.put(probe.getName(), probe);
        List<ExpressionProbe> pList = probeLists.get(probe.getChr());
        if (pList == null) {
            pList = new ArrayList(1000);
            probeLists.put(probe.getChr(), pList);
        }
        pList.add(probe);
    }

    public void sortProbeLists() {
        for (List<ExpressionProbe> list : probeLists.values()) {
            Collections.sort(list);
        }
    }


}