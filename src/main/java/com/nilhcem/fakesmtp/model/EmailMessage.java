package com.nilhcem.fakesmtp.model;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class EmailMessage {
    private String to;
    private String from;
    private String subject;
    private String contents;

    public EmailMessage(String to, String from, String subject, String contents) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.contents = contents;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmailMessage that = (EmailMessage) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(to, that.to)
                .append(from, that.from)
                .append(subject, that.subject)
                .append(contents, that.contents)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(to)
                .append(from)
                .append(subject)
                .append(contents)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("to", to)
                .append("from", from)
                .append("subject", subject)
                .append("contents", contents)
                .toString();
    }
}
