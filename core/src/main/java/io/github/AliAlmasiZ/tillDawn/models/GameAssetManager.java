package io.github.AliAlmasiZ.tillDawn.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class GameAssetManager {
    public final AssetManager manager = new AssetManager();
    private static GameAssetManager gameAssetManager;

    //Assets
    //Skins
    public final Skin flatEarthSkin;
    public final Skin pixthulhuuiSkin;

    //Animations
    public Animation<TextureRegion> characterIdleAnim;
    public Animation<TextureRegion> characterRunAnim;
    public Animation<TextureRegion> brainMonsterAnim;


    //Textures
    public Texture xpOrbTex;
    public Texture bulletTex;
    public Texture backgroundTileTex;


    //Paths
    private final String flatEarthSkinPath = "Flat_Earth_UI_Skin/flatearthui/flat-earth-ui.json";
    private final String pixthulhuuiSkinPath = "Pixthulhu_UI_Skin/pixthulhuui/pixthulhu-ui.json";
    //Character 1
    private final String character1Idle0 = "Images/Sprite/Idle_0.png";
    private final String character1Idle1 = "Images/Sprite/Idle_1 #8354.png";
    private final String character1Idle2 = "Images/Sprite/Idle_2 #8813.png";
    private final String character1Idle3 = "Images/Sprite/Idle_3.png";
    private final String character1Idle4 = "Images/Sprite/Idle_4.png";
    private final String character1Idle5 = "Images/Sprite/Idle_5.png";
    private final String character1Run0 = "Images/Sprite/Run_0 #8756.png";
    private final String character1Run1 = "Images/Sprite/Run_1 #8772.png";
    private final String character1Run2 = "Images/Sprite/Run_2.png";
    private final String character1Run3 = "Images/Sprite/Run_3.png";

    //Monsters
    private final String brainMonster0 = "Images/Sprite/BrainMonster_0.png";
    private final String brainMonster1 = "Images/Sprite/BrainMonster_1.png";
    private final String brainMonster2 = "Images/Sprite/BrainMonster_2.png";
    private final String brainMonster3 = "Images/Sprite/BrainMonster_3.png";

    //Other entities
    private final String bullet = "bullet.png";
    private final String xpOrb = "Images/Texture2D/T_SmallCircle.png";
    private final String backgroundTile = "Images/Sprite/T_TempleTile_3.png";
















    private GameAssetManager() {
        // Load UI skins
        manager.load(flatEarthSkinPath, Skin.class);
        manager.load(pixthulhuuiSkinPath, Skin.class);

        // Load character textures
        manager.load(character1Idle0, Texture.class);
        manager.load(character1Idle1, Texture.class);
        manager.load(character1Idle2, Texture.class);
        manager.load(character1Idle3, Texture.class);
        manager.load(character1Idle4, Texture.class);
        manager.load(character1Idle5, Texture.class);
        manager.load(character1Run0, Texture.class);
        manager.load(character1Run1, Texture.class);
        manager.load(character1Run2, Texture.class);
        manager.load(character1Run3, Texture.class);

        manager.load(brainMonster0, Texture.class);
        manager.load(brainMonster1, Texture.class);
        manager.load(brainMonster2, Texture.class);
        manager.load(brainMonster3, Texture.class);

        manager.load(xpOrb, Texture.class);
        manager.load(bullet, Texture.class);
        manager.load(backgroundTile, Texture.class);

        manager.finishLoading();

        // Initialize skins
        flatEarthSkin = manager.get(flatEarthSkinPath, Skin.class);
        pixthulhuuiSkin = manager.get(pixthulhuuiSkinPath, Skin.class);


        // Create animations
        Array<TextureRegion> idleFrames = new Array<>();
        idleFrames.add(new TextureRegion(manager.get(character1Idle0, Texture.class)));
        idleFrames.add(new TextureRegion(manager.get(character1Idle1, Texture.class)));
        idleFrames.add(new TextureRegion(manager.get(character1Idle2, Texture.class)));
        idleFrames.add(new TextureRegion(manager.get(character1Idle3, Texture.class)));
        idleFrames.add(new TextureRegion(manager.get(character1Idle4, Texture.class)));
        idleFrames.add(new TextureRegion(manager.get(character1Idle5, Texture.class)));

        Array<TextureRegion> runFrames = new Array<>();
        runFrames.add(new TextureRegion(manager.get(character1Run0, Texture.class)));
        runFrames.add(new TextureRegion(manager.get(character1Run1, Texture.class)));
        runFrames.add(new TextureRegion(manager.get(character1Run2, Texture.class)));
        runFrames.add(new TextureRegion(manager.get(character1Run3, Texture.class)));

        Array<TextureRegion> brainMonsterFrames = new Array<>();
        brainMonsterFrames.add(new TextureRegion(manager.get(brainMonster0, Texture.class)));
        brainMonsterFrames.add(new TextureRegion(manager.get(brainMonster1, Texture.class)));
        brainMonsterFrames.add(new TextureRegion(manager.get(brainMonster2, Texture.class)));
        brainMonsterFrames.add(new TextureRegion(manager.get(brainMonster3, Texture.class)));


        // create Textures
        xpOrbTex = manager.get(xpOrb, Texture.class);
        bulletTex = manager.get(bullet, Texture.class);
        backgroundTileTex = manager.get(backgroundTile, Texture.class);

        characterIdleAnim = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);
        characterRunAnim = new Animation<>(0.1f, runFrames, Animation.PlayMode.LOOP);
        brainMonsterAnim = new Animation<>(0.1f, brainMonsterFrames, Animation.PlayMode.LOOP);
    }

    public void dispose() {
        manager.dispose();
    }

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null)
            gameAssetManager = new GameAssetManager();
        return gameAssetManager;
    }


    public TextureRegion getCharacterIdleFrame() {
        // Return the first frame of idle animation or a specific idle TextureRegion
        if (characterIdleAnim != null && characterIdleAnim.getKeyFrames().length > 0) {
            return characterIdleAnim.getKeyFrame(0); // First frame of idle
        }
        // Fallback or error
        return null;
    }


    public String getCharacter1Idle0() {
        return character1Idle0;
    }

    public Animation<TextureRegion> getCharacterIdleAnim() {
        return characterIdleAnim;
    }

    public Animation<TextureRegion> getCharacterRunAnim() {
        return characterRunAnim;
    }
}
