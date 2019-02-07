package app.ptd.server.remoteresources;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ResourceImpl implements Resource, Closeable {

    private final InputStream content;

    private final boolean modified;

    private Optional<String> fingerprint;

    public ResourceImpl() {
        this.content = null;
        this.modified = false;
    }

    public ResourceImpl(InputStream content) {
        this.content = content;
        this.modified = true;
    }

    @Override
    public InputStream content() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return content != null;
    }

    @Override
    public boolean modified() {
        return modified;
    }

    public Optional<String> fingerprint() {
        return fingerprint;
    }

    public Resource fingerprint(String fingerprint) {
        this.fingerprint = Optional.ofNullable(fingerprint);
        return this;
    }

    @Override
    public void close() throws IOException {
        if (content != null) {
            content.close();
        }
    }

}
