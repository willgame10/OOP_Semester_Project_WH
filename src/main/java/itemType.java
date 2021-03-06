/**
 * Author: William Howell
 * Brief: Enum that stores all of the product types and their 2 letter signature.
 */
enum ItemType {
    AUDIO("AU"),
    VISUAL("VI"),
    AUDIO_MOBILE("AM"),
    VISUAL_MOBILE("VM");

    public final String code;

    ItemType(String code) {
        this.code = code;
    }

}