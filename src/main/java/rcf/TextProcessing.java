package rcf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Lieshoff, 11.11.15
 */
public class TextProcessing {

    private final List<Processor> processors;

    public TextProcessing(List<Processor> processors) {
        this.processors = processors;
    }

    public void start(String source) {
        for (Processor processor : processors) {
            processor.process(source);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<Processor> processors = new ArrayList<>();

        public TextProcessing build() {
            return new TextProcessing(processors);
        }

        public Builder one(String startTag, String endTag, Action action) {
            processors.add(new Processor(startTag, endTag, action));
            return this;
        }

        public Builder multi(String startTag, String endTag, Action action) {
            processors.add(new MultiProcessor(startTag, endTag, action));
            return this;
        }

        public Builder chain(String startTag, String endTag, Action action) {
            processors.get(processors.size() - 1).chain(new Processor(startTag, endTag, action));
            return this;
        }

        public Builder chainMulti(String startTag, String endTag, Action action) {
            processors.get(processors.size() - 1).chain(new MultiProcessor(startTag, endTag, action));
            return this;
        }

    }

    private static class Processor {
        List<Processor> processors = new ArrayList<>();
        String startTag;
        String endTag;
        Action action;
        int startTagLength;

        private Processor(String startTag, String endTag, Action action) {
            this.startTag = startTag;
            this.endTag = endTag;
            this.action = action;
            startTagLength = startTag == null ? 0 : startTag.length();
        }

        private void chain(Processor processor) {
            processors.add(processor);
        }

        void process(String source) {
            int start = source.indexOf(startTag == null ? "" : startTag);
            if (start >= 0) {
                int end = source.indexOf(endTag, start + startTagLength);
                if (end >= 0) {
                    String sub = source.substring(start + startTagLength, end);
                    action.execute(sub);
                    for (Processor processor : processors) {
                        processor.process(sub);
                    }
                } else {
                    // end not found
                }
            } else {
                // start not found
            }
        }

    }

    private static class MultiProcessor extends Processor {
        private MultiProcessor(String startTag, String endTag, Action action) {
            super(startTag, endTag, action);
        }

        @Override
        void process(String source) {
            int start = source.indexOf(startTag);
            if (start >= 0) {
                while (start >= 0) {
                    int end = source.indexOf(endTag, start + startTagLength);
                    if (end >= 0) {
                        String sub = source.substring(start + startTagLength, end);
                        action.execute(sub);
                        for (Processor processor : processors) {
                            processor.process(sub);
                        }
                        start = source.indexOf(startTag, end + endTag.length() - 1);
                    } else {
                        // end not found
                        break;
                    }
                }
            }
        }
    }

}
