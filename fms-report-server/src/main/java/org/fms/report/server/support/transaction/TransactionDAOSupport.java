/**
 * Auth:riozenc
 * Date:2018年5月10日 上午10:29:32
 * Title:cis.web.support.transaction.TransactionDAOSupport.java
 **/
package org.fms.report.server.support.transaction;

import org.springframework.context.annotation.Configuration;

import com.riozenc.titanTool.spring.transaction.registry.TransactionDAORegistryPostProcessor;

@Configuration
public class TransactionDAOSupport extends TransactionDAORegistryPostProcessor {

}
