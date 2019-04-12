package com.luoye.whr.pixivGallery.common

import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.ArrayList

class HttpDns : Dns {
    /*著作权归作者所有。
        商业转载请联系作者获得授权，非商业转载请注明出处。
        Mashiro
        链接：https://2heng.xin/2017/09/19/pixiv/
        来源：樱花庄的白猫

        #Pixiv Start
        210.129.120.49  pixiv.net
        210.129.120.49  www.pixiv.net
        210.140.92.134  i.pximg.net
        210.140.131.146 source.pixiv.net
        210.129.120.56  accounts.pixiv.net
        210.129.120.56  touch.pixiv.net
        210.140.131.147 imgaz.pixiv.net
        210.129.120.44  app-api.pixiv.net
        210.129.120.48  oauth.secure.pixiv.net
        210.129.120.41  dic.pixiv.net
        210.140.131.153 comic.pixiv.net
        210.129.120.43  factory.pixiv.net
        74.120.148.207  g-client-proxy.pixiv.net
        210.140.174.37  sketch.pixiv.net
        210.129.120.43  payment.pixiv.net
        210.129.120.41  sensei.pixiv.net
        210.140.131.144 novel.pixiv.net
        210.129.120.44  en-dic.pixiv.net
        210.140.131.145 i1.pixiv.net
        210.140.131.145 i2.pixiv.net
        210.140.131.145 i3.pixiv.net
        210.140.131.145 i4.pixiv.net
        210.140.131.159 d.pixiv.org
        210.140.92.141  s.pximg.net
        210.140.92.135  pixiv.pximg.net
        210.129.120.56  fanbox.pixiv.net
        #Pixiv End*/
    private val addresses = arrayOf("210.129.120.50", "210.140.92.135", "210.140.131.144", "210.129.120.46", "210.140.131.144", "210.140.131.147")
//    private String[] addresses = {"123.207.137.88", "202.141.162.123", "123.207.56.160", "115.159.220.214"};

    @Throws(UnknownHostException::class)
    override fun lookup(paramString: String): List<InetAddress> {
        try {
            val localArrayList = ArrayList<InetAddress>()
            val arrayOfString = this.addresses
            val j = 0
            var i = j
            i = this.addresses.size
            i = j
            while (i < this.addresses.size) {
                localArrayList.add(InetAddress.getByName(this.addresses[i]))
                i += 1
            }
            return localArrayList
        } catch (localException: Exception) {
            localException.printStackTrace()
        }

        return Dns.SYSTEM.lookup(paramString)
    }
}