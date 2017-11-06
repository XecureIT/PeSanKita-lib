/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */

package org.whispersystems.signalservice.api.messages;

import java.io.InputStream;

public abstract class SignalServiceAttachment {

  private final String contentType;
  private final String filename;

  protected SignalServiceAttachment(String contentType, String filename) {
    this.contentType = contentType;
    this.filename = filename;
  }

  public String getContentType() {
    return contentType;
  }
  
  public String getFilename() {
    return filename;
  }

  public abstract boolean isStream();
  public abstract boolean isPointer();

  public SignalServiceAttachmentStream asStream() {
    return (SignalServiceAttachmentStream)this;
  }

  public SignalServiceAttachmentPointer asPointer() {
    return (SignalServiceAttachmentPointer)this;
  }

  public static Builder newStreamBuilder() {
    return new Builder();
  }

  public static class Builder {

    private InputStream      inputStream;
    private String           contentType;
    private String           filename;
    private long             length;
    private ProgressListener listener;

    private Builder() {}

    public Builder withStream(InputStream inputStream) {
      this.inputStream = inputStream;
      return this;
    }

    public Builder withContentType(String contentType) {
      this.contentType = contentType;
      return this;
    }
    
    public Builder withFilename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder withLength(long length) {
      this.length = length;
      return this;
    }

    public Builder withListener(ProgressListener listener) {
      this.listener = listener;
      return this;
    }

    public SignalServiceAttachmentStream build() {
      if (inputStream == null) throw new IllegalArgumentException("Must specify stream!");
      if (contentType == null) throw new IllegalArgumentException("No content type specified!");
      //if (filename == null) throw new IllegalArgumentException("No filename specified!");
      if (length == 0)         throw new IllegalArgumentException("No length specified!");

      return new SignalServiceAttachmentStream(inputStream, contentType, filename, length, listener);
    }
  }

  /**
   * An interface to receive progress information on upload/download of
   * an attachment.
   */
  public interface ProgressListener {
    /**
     * Called on a progress change event.
     *
     * @param total The total amount to transmit/receive in bytes.
     * @param progress The amount that has been transmitted/received in bytes thus far
     */
    public void onAttachmentProgress(long total, long progress);
  }
}
