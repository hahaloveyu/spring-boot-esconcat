package com.liyuze;

import com.alibaba.fastjson.JSONObject;
import com.liyuze.utils.ElasticsearchUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import sun.java2d.pipe.SpanIterator;

import static com.liyuze.utils.ElasticsearchUtils.addData;
import static com.liyuze.utils.ElasticsearchUtils.createIndex;
import static com.liyuze.utils.ElasticsearchUtils.isIndexExist;

@RunWith(SpringRunner.class)
@SpringBootTest

public class SpringBootEsconcatApplicationTests {

	@Test
	public void contextLoads()  {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("xiaoyang","keyide");
		jsonObject.put("bookname","liyuze");
		String s = addData(jsonObject, "liyuze", "liyuze");
		//boolean liyuze = createIndex("liyuze");
		System.out.println("返回值为++++++++++"+s);
	}

}
