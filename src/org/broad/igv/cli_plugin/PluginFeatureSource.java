/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.cli_plugin;

import org.apache.log4j.Logger;
import org.broad.igv.feature.LocusScore;
import org.broad.igv.sam.Alignment;
import org.broad.igv.sam.AlignmentInterval;
import org.broad.igv.sam.AlignmentTrack;
import org.broad.igv.session.SubtlyImportant;
import org.broad.igv.track.FeatureSource;
import org.broad.igv.track.FeatureTrack;
import org.broad.igv.track.Track;
import org.broad.tribble.Feature;

import java.io.IOException;
import java.util.*;

/**
 * A feature source which derives its information
 * from a command line cli_plugin
 * User: jacob
 * Date: 2012/05/01
 */
public class PluginFeatureSource extends PluginSource implements FeatureSource<Feature>{

    private static Logger log = Logger.getLogger(PluginFeatureSource.class);

    @SubtlyImportant
    private PluginFeatureSource(){}

    public PluginFeatureSource(List<String> commands, LinkedHashMap<Argument, Object> arguments, PluginSpecReader.Parser parsingAttrs, String specPath) {
        super(commands, arguments, parsingAttrs, specPath);
    }

    @Override
    protected String createTempFile(Track track, Argument argument, String chr, int start, int end, int zoom) throws IOException {
        if(track instanceof AlignmentTrack){
            return createTempFileForAlignmentTrack((AlignmentTrack) track, argument, chr, start, end, zoom);
        }

        FeatureTrack fTrack = (FeatureTrack) track;
        List<Feature> features = fTrack.getFeatures(chr, start, end);
        return super.createTempFile(features, argument);
    }

    private String createTempFileForAlignmentTrack(AlignmentTrack track, Argument argument, String chr, int start, int end, int zoom) throws IOException {

        Collection<AlignmentInterval> loadedIntervals = track.getDataManager().getAllLoadedIntervals();
        List<Alignment> alignments = new ArrayList<Alignment>();
        for(AlignmentInterval interval: loadedIntervals){
            if(interval.overlaps(chr, start, end, zoom)){
                Iterator<Alignment> iter = interval.getAlignmentIterator();
                while(iter.hasNext()){
                    Alignment al = iter.next();
                    if(al.getStart() <= end && al.getEnd() >= start){
                        alignments.add(al);
                    }
                }
            }
        }

        return super.createTempFile(alignments, argument);
    }

    /**
     * Perform the actual combination operation between the constituent data
     * sources. This implementation re-runs the operation each call.
     *
     * @param chr
     * @param start
     * @param end
     * @return
     * @throws java.io.IOException
     */
    @Override
    public final Iterator<Feature> getFeatures(String chr, int start, int end) throws IOException {
        return super.getFeatures(chr, start, end, -1);
    }

    @Override
    public List<LocusScore> getCoverageScores(String chr, int start, int end, int zoom) {
        return null;
    }

    /**
     * If this track has not had it's feature window size set,
     * we use the minimum of the sources
     *
     * @return
     */
    @Override
    public final int getFeatureWindowSize() {
        int featureWindowSize = getMinSizeFromTracks(arguments.values());
        return featureWindowSize;
    }

    private int getMinSizeFromTracks(Iterable tracks) {
        int featureWindowSize = Integer.MAX_VALUE;
        for (Object val : tracks) {
            int tmpSize = Integer.MAX_VALUE;
            if (val instanceof FeatureTrack) {
                FeatureTrack track = ((FeatureTrack) val);
                tmpSize = track.getFeatureWindowSize();
            } else if (val instanceof List) {
                featureWindowSize = getMinSizeFromTracks((List) val);
            }
            featureWindowSize = Math.min(featureWindowSize, tmpSize);
        }
        return featureWindowSize;
    }

    @Override
    public final void setFeatureWindowSize(int size) {
        //no-op
    }


}
