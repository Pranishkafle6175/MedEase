package com.example.medease.Model;

import java.util.List;

public class ParentItem {

    // Declaration of the variables
    private String ParentItemTitle;
    private List<Products> productsList;

    // Constructor of the class
    // to initialize the variables
    public ParentItem(
            String ParentItemTitle,
            List<Products> productsList)
    {

        this.ParentItemTitle = ParentItemTitle;
        this.productsList = productsList;
    }

    // Getter and Setter methods
    // for each parameter
    public String getParentItemTitle()
    {
        return ParentItemTitle;
    }

    public void setParentItemTitle(
            String parentItemTitle)
    {
        ParentItemTitle = parentItemTitle;
    }

    public List<Products> getChildItemList()
    {
        return productsList;
    }

    public void setChildItemList(
            List<Products> childItemList)
    {
        productsList = childItemList;
    }
}
