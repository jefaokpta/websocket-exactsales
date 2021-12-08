package br.com.vip.websocketexactsales.model

/**
 * @author Jefferson Alves Reis (jefaokpta) < jefaokpta@hotmail.com >
 * Date: 08/12/21
 */
class UserCentral {
    companion object{

        val users = mutableMapOf<Int, MutableMap<Int, User>>()

        fun setUser(user: User){
            if(users.containsKey(user.orgId)){
                users[user.orgId]!![user.userId] = user
            }else{
                users[user.orgId] = mutableMapOf(user.userId to user)
            }
        }
    }

}