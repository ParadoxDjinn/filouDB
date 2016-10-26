/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filou.observe;

/**
 *
 * @author dark
 * @param <O> Observable
 */
public interface Observable<O extends Observable<O>> {

  public <T extends ChangeEvent<O>> void addListener(ChangeListener<O, T> listener);

  public <T extends ChangeEvent<O>> void removeListener(ChangeListener<O, T> listener);

  public ChangeEvent<O> changeEvent();
}
