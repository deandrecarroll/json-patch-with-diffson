import cats.implicits._
import diffson.jsonpatch.JsonPatch
import diffson.playJson.DiffsonProtocol._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.util.{Failure, Success, Try}

/* We have a user model that is defined below, a user who can have various roles in the system.
   We'll define a member role and a captain role.
*/

case class User(firstName: String, lastName: String, email: String, roles: Seq[UserRole])
case class UserRole(role: String)

val memberRole: UserRole = UserRole("member")
val captainRole: UserRole = UserRole("captain")

// We have our JSON serializer/deserializer pair defined in our standard ways

implicit val _user_role_format = Json.format[UserRole]
implicit val _user_format = Json.format[User]

// We'll define the following user in our system, who only starts out as a member.

val standardUser: User = User("Arthur", "Dent", "adent@earth.com", Seq(memberRole))

/*
  Serialized as JSON, the user looks like this:

  {
  "firstName": "Arthur",
  "lastName": "Dent",
  "email": "adent@earth.com",
  "roles": [
      {
        "role": "member"
      }
    ]
  }

  We want to add a captain role to the user and update the user's email address from
  'adent@earth.com' to 'adent@heartofgold.com'.

  The JSON PATCH request body looks like so:
*/

val patchJson: JsValue = Json.parse(
  """
    |[
    |    {
    |      "op": "replace",
    |      "path": "/email",
    |      "value": "adent@heartofgold.com"
    |    },
    |    {
    |      "op": "add",
    |      "path": "/roles/0",
    |      "value": {
    |        "role": "captain"
    |      }
    |    }
    |]
  """.stripMargin)

/*

  After the JSON PATCH is applied, the resulting JSON will look like this:

  {
    "firstName": "Arthur",
    "lastName": "Dent",
    "email": "adent@heartofgold.com",
    "roles": [
      {
        "role": "captain"
      },
      {
        "role": "member"
      }
    ]
  }

  ...and will be deserialized to a User object like so:

    User(Arthur,Dent,adent@heartofgold.com,List(UserRole(captain), UserRole(member)))

  Take note that we have to serialize the original User object to JSON in order to apply the patch. We must
  then deserialize the patch result from JSON to get our new updated user case class.

*/

val userUpdate: Try[User] = (patchJson.validate[JsonPatch[JsValue]] match { // Deserialize JSON PATCH request
  // The JSON PATCH request is applied to a JSON serialized version of the current user data.
  // The patch application takes the form: patchRequest[FailureMonadType](json body of object to be updated)
  case JsSuccess(jsonPatchRequest, _) => jsonPatchRequest[Try](Json.toJson(standardUser))
  case JsError(_) => Failure(new Exception("Invalid patch request format"))

}) flatMap { updatedResult =>
  println(updatedResult) //Show serialized updated user

  //Next, we need to deserialize the result of the JSON PATCH to obtain the updated user
  updatedResult.validate[User] match {
    case JsSuccess(updatedUser, _) => Success(updatedUser)
    case JsError(_) => Failure(new Exception("Couldn't deserialize updated user"))
  }
}

println(userUpdate.get)

