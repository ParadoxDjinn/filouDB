/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filou.util;

import filou.entries.StructEntry;
import filou.media.Register;

/**
 *
 * @author dark
 */
public interface SelfStorable {

  public String key();

  public StructEntry save(Register register);

}
