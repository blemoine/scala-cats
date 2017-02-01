package com.mnubo.example


import cats.data.NonEmptyList
import cats.instances.all._

import scala.concurrent.Future


object Example {

/*
  def isPositive(i: Int): Option[Int] = if(i > 0) Some(i) else None
  def nonEmpty(s: String): Option[String] =
    if (!s.isEmpty) Some(s) else None

  def validate(id:Int, name:String):Option[User] = for {
    okId  <- isPositive(id)
    okName <- nonEmpty(name)
  } yield User(okId, okName)

  validate( 1, "Georges") // Some(User(1, "Georges"))
  validate( 1, "")        // None
  validate(-1, "Georges") // None
  validate(-1, "")        // None

  NonEmptyList.of(1,2, 3).toList.mkString


  import cats.Functor
  import cats.implicits._
  def add1[M[_]: Functor](o: M[Int]):M[Int] = o.map(_ + 1)

*/
  sealed trait ApiResponse[+A]
  case class ApiSuccess[A](content: A) extends ApiResponse[A]
  case class ApiError(status: Int) extends ApiResponse[Nothing]




  import cats._
  import cats.implicits._
  /*
  implicit val functorApiResponse: Functor[ApiResponse] = new Functor[ApiResponse] {
    override def map[A, B](fa: ApiResponse[A])(f: (A) => B):  ApiResponse[B] = fa match {
      case ApiSuccess(content) => ApiSuccess(f(content))
      case err@ApiError(_, _) => err
    }
  }
  */
  implicit val  applicativeApiResponse: Applicative[ApiResponse] = new Applicative[ApiResponse] {
    override def pure[A](a: A): ApiResponse[A] = ApiSuccess(a)

    override def ap[A, B](ff: ApiResponse[(A) => B])(fa: ApiResponse[A]): ApiResponse[B] = fa match {
      case ApiSuccess(content) => ff match {
        case ApiSuccess(f) => ApiSuccess(f(content))
        case err@ApiError(_) => err
      }
      case err@ApiError(_) => err
    }
  }


  val l: List[ApiResponse[Int]] = List(ApiSuccess(1),ApiSuccess(10),ApiSuccess(100))
  val s:ApiResponse[List[Int]] = l.sequence
  l.head.map(_ + 1)


}