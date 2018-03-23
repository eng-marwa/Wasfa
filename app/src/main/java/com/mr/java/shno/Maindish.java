
package com.mr.java.shno;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;


public class Maindish implements Parcelable,Comparable<Maindish> {
    private Integer id;
    private String name;
    private String image;
    private String tag;
    private int serving;
    private String cookTime;
    private String category;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private List<String> ingredients = null;
    private List<String> steps = null;

    public Maindish() {
    }

    public Maindish(Integer id, String name, String image, String tag, String category,int serving ,String cookTime, List<String> ingredients, List<String> steps, String key) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.tag = tag;
        this.category = category;
        this.serving = serving;
        this.cookTime = cookTime;
        this.ingredients = ingredients;
        this.steps = steps;
        this.key = key;
    }

    public Maindish( String name, String image, String tag, int serving, String cookTime, String category, List<String> ingredients, List<String> steps) {

        this.name = name;
        this.image = image;
        this.tag = tag;
        this.serving = serving;
        this.cookTime = cookTime;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;

    }

    protected Maindish(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        image = in.readString();
        tag = in.readString();
        serving = in.readInt();
        cookTime = in.readString();
        category = in.readString();
        ingredients = in.createStringArrayList();
        steps = in.createStringArrayList();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(tag);
        dest.writeInt(serving);
        dest.writeString(cookTime);
        dest.writeString(category);
        dest.writeStringList(ingredients);
        dest.writeStringList(steps);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Maindish> CREATOR = new Creator<Maindish>() {
        @Override
        public Maindish createFromParcel(Parcel in) {
            return new Maindish(in);
        }

        @Override
        public Maindish[] newArray(int size) {
            return new Maindish[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Maindish maindish) {
        return this.name.compareTo(maindish.getName());
    }
}
