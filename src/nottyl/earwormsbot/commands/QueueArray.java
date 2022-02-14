package nottyl.earwormsbot.commands;

import java.util.ArrayList;

public class QueueArray<AudioTrack> extends ArrayList<AudioTrack> {

    public AudioTrack popHead() throws IllegalStateException{
        try {
            return this.remove(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("Error: Track requested doesn't exist.");
        }
    }

    public AudioTrack popTail() throws IllegalStateException{
        try {
            return this.remove(this.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("Error: Track requested doesn't exist");
        }
    }

    public void pushHead(AudioTrack track) {
        this.add(0, track);
    }

    public void pushTail(AudioTrack track) {
        this.add(this.size(), track);
    }

}

