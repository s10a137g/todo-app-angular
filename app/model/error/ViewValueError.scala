package model.error

import model.ViewValueCommon

case class ViewValueError (
  val title:  String,      
  val cssSrc: Seq[String], 
  val jsSrc:  Seq[String] 
) extends ViewValueCommon 
