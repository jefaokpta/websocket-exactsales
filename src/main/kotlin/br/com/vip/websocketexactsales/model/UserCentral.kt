package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class UserCentral {
    companion object{

        val users = mutableMapOf<String, MutableMap<String, User>>()

        fun setUser(user: User){
            if(users.containsKey(user.orgId)){
                users[user.orgId]!!["${user.orgId}-${user.userId}"] = user
            }else{
                users[user.orgId] = mutableMapOf("${user.orgId}-${user.userId}" to user)
            }
        }
        fun getUser(orgId: String, userId: String): User?{
            return users[orgId]?.get("$orgId-$userId")
        }
        fun removeUser(orgId: String, userId: String){
            users[orgId]?.remove("$orgId-$userId")
        }
    }

}