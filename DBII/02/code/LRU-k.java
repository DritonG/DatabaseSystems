import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class LRUk {
    private final int k;
    private final Buffer buffer;

    public static void main(String[] args) {
        final int K;
        if (args.length == 1) {
            K = Integer.parseInt(args[0]);
        } else {
            K = 2;
        }

        final int FRAMES = 11;
        final Buffer buffer = new Buffer(FRAMES);

        final int PAGES = 100;
        final PageStore pages = new PageStore(PAGES);


        final LinkedList<PageStore.Page> pagePinSequence = new LinkedList<PageStore.Page>();
        for (int i = 0; i < pages.size(); i++) {
            //T1
            pagePinSequence.addLast(pages.get(i));
            //T2
            pagePinSequence.addLast(pages.get(i % 10));
        }

        new LRUk(K, buffer, pages).run(pagePinSequence);
    }

    public LRUk(final int k, Buffer buffer, final PageStore pages) {
        this.k = k;
        this.buffer = buffer;
    }

    public void run(final LinkedList<PageStore.Page> pagePinSequence) {
        int hits = 0;
        int misses = 0;

        for (PageStore.Page pageToPin : pagePinSequence) {
            if (buffer.isBuffered(pageToPin)) {
                buffer.buffer(pageToPin);
                hits++;
            } else {
                if (buffer.hasFreeFrame()) {
                    buffer.buffer(pageToPin);
                } else {
                    final PageStore.Page victim = defineVictim(pagePinSequence, hits + misses);
                    buffer.buffer(victim, pageToPin);
                    System.out.printf("victim=%s\n", victim);
                }
                misses++;
            }

            buffer.print();
            System.out.println();
        }
        System.out.printf("hits: %d, misses: %d\n", hits, misses);
    }

    public PageStore.Page defineVictim(LinkedList<PageStore.Page> pagePinSequence, final int t) {

        HashMap<Integer, PageStore.Page> distancePerPage = new HashMap<Integer, PageStore.Page>();
        for (PageStore.Page bufferedPage : buffer) {
            final int distance = backwardKDistance(t, pagePinSequence, bufferedPage);
            if (!distancePerPage.containsKey(distance))
                distancePerPage.put(distance, bufferedPage);
        }

        int maxDistance = Collections.max(distancePerPage.keySet());
        return distancePerPage.get(maxDistance);
    }

    private int backwardKDistance(final int t, final LinkedList<PageStore.Page> pagePinSequence, final PageStore.Page frame) {
        int x = 0;
        int k = this.k;
        for (int j = t - 1; j >= 0; j--) {
            x++;
            if (pagePinSequence.get(j) == frame) {
                k--;
            }
            if (k == 0) {
                break;
            }
        }
        if (k != 0) {
            x = Integer.MAX_VALUE;
        }
        return x;
    }

    private static class Buffer implements Iterable<PageStore.Page> {
        private static final int UNDEFINED = -1;

        private final PageStore.Page[] frames;
        private final int MOST_RECENT;
        private int leastRecent = UNDEFINED;

        Buffer(final int size) {
            MOST_RECENT = size - 1;
            frames = new PageStore.Page[size];
        }

        public void buffer(final PageStore.Page page) {
            if (isEmpty()) {
                leastRecent = MOST_RECENT;
            } else if (isBuffered(page)) {
                final int pageBuffer = getPageBuffer(page);
                if (pageBuffer < MOST_RECENT) {
                    shiftBufferedPages(pageBuffer);
                }
            } else if (hasFreeFrame()) {
                shiftBufferedPages(leastRecent);
            }
            frames[MOST_RECENT] = page;
        }

        public void buffer(final PageStore.Page victim, final PageStore.Page page) {
            final int pageBuffer = getPageBuffer(victim);
            if (pageBuffer < MOST_RECENT) {
                shiftBufferedPages(pageBuffer + 1);
            }
            frames[MOST_RECENT] = page;
        }

        private void shiftBufferedPages(final int index) {
            int i = index == 0 ? 1 : index;
            for (; i <= MOST_RECENT; i++) {
                frames[i - 1] = frames[i];
            }
            if (leastRecent > 0) {
                leastRecent--;
            }
        }

        public boolean isBuffered(final PageStore.Page page) {
            return getPageBuffer(page) != UNDEFINED;
        }

        public boolean hasFreeFrame() {
            return isEmpty() || leastRecent != 0;
        }

        private int getPageBuffer(final PageStore.Page page) {
            if (!isEmpty()) {
                for (int i = MOST_RECENT; i >= leastRecent; i--) {
                    if (frames[i] == page) {
                        return i;
                    }
                }
            }
            return UNDEFINED;
        }

        private boolean isEmpty() {
            return leastRecent == UNDEFINED;
        }

        public PageStore.Page[] getFrames() {
            return frames;
        }

        public void print() {
            if (!isEmpty()) {
                final StringBuffer head = new StringBuffer();
                final StringBuffer data = new StringBuffer();
                for (int i = leastRecent; i <= MOST_RECENT; i++) {
                    head.append(i + 1 + "\t");
                    data.append(frames[i] + "\t");
                }
                System.out.println(head);
                System.out.println(data);
            }
        }

        public Iterator<PageStore.Page> iterator() {
            return new Iterator<PageStore.Page>() {
                private int current = leastRecent;

                @Override
                public boolean hasNext() {
                    return current != MOST_RECENT;
                }

                @Override
                public PageStore.Page next() {
                    if (current > MOST_RECENT) {
                        return null;
                    }
                    return frames[current++];
                }

                @Override
                public void remove() {

                }
            };
        }
    }

    private static class PageStore {
        private final Page[] pages;

        public PageStore(final int size) {
            pages = new Page[size];
            for (int i = 0; i < size; i++) {
                pages[i] = new Page();
            }
        }

        public Page get(final int index) {
            return pages[index];
        }

        public int size() {
            return pages.length;
        }

        private static class Page {
            private static int instances = 0;
            private int pageNumber = ++instances;

            @Override
            public String toString() {
                return Integer.toString(pageNumber);
            }
        }

    }
}
