package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class UserCentral {
    companion object{

        private val users = mutableMapOf<String, User>()

        fun setUser(user: User){
            users["${user.orgId}-${user.userId}"] = user
        }

        fun getUser(session: String): User?{
            return users[session]
        }

        fun getUser(orgId: String, userId: String): User?{
            return users[ "$orgId-$userId" ]
        }

        fun removeUser(session: String){
            users.remove(session)
        }

        fun listAllByOrgId(orgId: String) =
            users.filter { it.value.orgId == orgId }
            .values.toList()

    }

}