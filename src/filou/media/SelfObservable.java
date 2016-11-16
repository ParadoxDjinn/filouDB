/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filou.media;

/**
 *
 * @author dark
 * @param <O> SelfObservable
 */
public interface SelfObservable<O extends SelfObservable<O>> {

  public void init(Entry<O> entry);

  public void uninit(Entry<O> entry);
}
