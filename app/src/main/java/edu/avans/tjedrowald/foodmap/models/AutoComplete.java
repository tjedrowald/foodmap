package edu.avans.tjedrowald.foodmap.models;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.util.ArrayList;

public class AutoComplete implements Serializable {
    @JsonGetter("terms")
    public ArrayList<Term> getTerms() {
        return this.terms;
    }
    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }
    ArrayList<Term> terms;

    @JsonGetter("businesses")
    public ArrayList<Business> getBusinesses() {
        return this.businesses;
    }
    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }
    ArrayList<Business> businesses;

    @JsonGetter("categories")
    public ArrayList<Category> getCategories() {
        return this.categories;
    }
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
    ArrayList<Category> categories;
}
