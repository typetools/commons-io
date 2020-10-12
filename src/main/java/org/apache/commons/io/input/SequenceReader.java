/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

/**
 * Provides the contents of multiple Readers in sequence.
 *
 * @since 2.7
 */
public class SequenceReader extends Reader {

    private @Nullable Reader reader;
    // null only after this SequenceReader has been closed
    private @Nullable Iterator<? extends Reader> readers;

    /**
     * Construct a new instance with readers
     *
     * @param readers the readers to read
     */
    @EnsuresNonNull("this.readers")
    public SequenceReader(final Iterable<? extends Reader> readers) {
        this.readers = Objects.requireNonNull(readers, "readers").iterator();
        this.reader = nextReader();
    }

    /**
     * Construct a new instance with readers
     *
     * @param readers the readers to read
     */
    public SequenceReader(final Reader... readers) {
        this(Arrays.asList(readers));
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Reader#close()
     */
    @Override
    public void close() throws IOException {
        this.readers = null;
        this.reader = null;
    }

    /**
     * Returns the next available reader or null if done.
     *
     * @return the next available reader or null
     */
    @RequiresNonNull("this.readers")
    @EnsuresNonNull("this.readers")
    private @Nullable Reader nextReader(@UnknownInitialization(SequenceReader.class) SequenceReader this) {
        return this.readers.hasNext() ? this.readers.next() : null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Reader#read(char[], int, int)
     */
    @SuppressWarnings({
        "nullness:contracts.precondition.not.satisfied", // reader.read() does not unset this.readers
        "nullness:contracts.precondition.override.invalid" // extra @RequiresNonNull
    })
    @RequiresNonNull("this.readers")
    @Override
    public int read() throws IOException {
        int c = EOF;
        while (reader != null) {
            c = reader.read();
            if (c == EOF) {
                reader = nextReader();
            } else {
                break;
            }
        }
        return c;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Reader#read()
     */
    @SuppressWarnings({
        "nullness:contracts.precondition.not.satisfied", // reader.read() does not unset this.readers
        "nullness:contracts.precondition.override.invalid" // extra @RequiresNonNull
    })
    @RequiresNonNull({"this.readers", "this.reader"})
    @Override
    public int read(final char[] cbuf, int off, int len) throws IOException {
        Objects.requireNonNull(cbuf, "cbuf");
        if (len < 0 || off < 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        int count = 0;
        while (reader != null) {
            final int readLen = reader.read(cbuf, off, len);
            if (readLen == EOF) {
                reader = nextReader();
            } else {
                count += readLen;
                off += readLen;
                len -= readLen;
                if (len <= 0) {
                    break;
                }
            }
        }
        if (count > 0) {
            return count;
        }
        return EOF;
    }
}