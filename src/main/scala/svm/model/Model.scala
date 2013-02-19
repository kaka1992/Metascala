package svm.model

import java.nio.ByteBuffer
import svm.model.ConstantInfo.{ClassRef, ZeroConstant}


object ClassFile{
  def read(implicit input: ByteBuffer) = {
    val magic = u4
    val minor_version = u2
    val major_version = u2

    implicit val constantPool = ConstantInfo.partitionConstantPool(u2-1).map(_())
    //constantPool.zipWithIndex.foreach(x => println(x._2 + "\t" + x._1))
    ClassFile(
      magic,
      minor_version,
      major_version,
      constantPool,
      u2,
      constantPool(u2).asInstanceOf[ClassRef],
      constantPool(u2).asInstanceOf[ClassRef],
      u2 ** constantPool(u2).asInstanceOf[ClassRef],
      u2 ** FieldInfo.read,
      u2 ** MethodInfo.read,
      u2 ** Attribute.read
    )
  }
}
case class ClassFile(magic: u4,
                     minor_version: u2,
                     major_version: u2,
                     constant_pool: Seq[Any],
                     access_flags: u2,
                     this_class: ClassRef,
                     super_class: ClassRef,
                     interfaces: Seq[ConstantInfo.ClassRef],
                     fields: Seq[FieldInfo],
                     methods: Seq[MethodInfo],
                     attributes: Seq[Attribute])


object FieldInfo{
  def read(implicit input: ByteBuffer, cp: Seq[Any]) = {
    FieldInfo(
      u2,
      cp(u2).asInstanceOf[String],
      cp(u2).asInstanceOf[String],
      u2**Attribute.read
    )
  }
}
case class FieldInfo(access_flags: u2,
                     name: String,
                     descriptor: String,
                     attributes: Seq[Attribute])

object MethodInfo{
  def read(implicit input: ByteBuffer, cp: Seq[Any]) = {
    MethodInfo(
      u2,
      cp(u2).asInstanceOf[String],
      cp(u2).asInstanceOf[String],
      u2 ** Attribute.read
    )
  }
}
case class MethodInfo(access_flags: u2,
                      name: String,
                      descriptor: String,
                      attributes: Seq[Attribute])


