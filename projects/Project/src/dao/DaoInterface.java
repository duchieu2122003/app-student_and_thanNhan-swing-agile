/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;

/**
 *
 * @author dinhv
 */
public interface DaoInterface<T> {

    public void insert(T t);

    public void update(T t, String masv);

    public void delete(T t);

    public ArrayList<T> selectAll();

    public T selectById(T t);

    public ArrayList<T> selectCondition(String condition);
}
