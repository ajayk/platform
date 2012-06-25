DELETE FROM UM_USER WHERE UM_USER_NAME LIKE 'provider%';
DELETE FROM UM_USER WHERE UM_USER_NAME LIKE 'subscriber%';

-- Adding provider 
INSERT INTO UM_USER (UM_USER_NAME ,UM_USER_PASSWORD ,UM_SALT_VALUE ,UM_REQUIRE_CHANGE , UM_CHANGED_TIME ,UM_TENANT_ID ) VALUES ('provider1','qsx45WkvN5vS0HCp4rGmTrRxaRsDF51VyjzzFHQjFmw=','wNZPdWDNF27y2ObAJNSchw==','FALSE',CURRENT_TIME(),-1234);

SELECT 1, SET(@USER_ID, SCOPE_IDENTITY());
INSERT INTO UM_USER_ROLE (UM_ROLE_ID, UM_USER_ID ,UM_TENANT_ID )  VALUES (1, @USER_ID,-1234);
INSERT INTO UM_USER_ROLE (UM_ROLE_ID, UM_USER_ID ,UM_TENANT_ID )  VALUES (2,@USER_ID,-1234);

-- Adding subscriber
INSERT INTO UM_USER (UM_USER_NAME ,UM_USER_PASSWORD ,UM_SALT_VALUE ,UM_REQUIRE_CHANGE , UM_CHANGED_TIME ,UM_TENANT_ID ) VALUES ('subscriber1','Mx5VpNXKTxHv2WrWUPj7iUR9IfWs4qN18xYUV3sxo94=','Q3En4ZN+pVGk6UYkTx6SDQ==','FALSE',CURRENT_TIME(),-1234);

SELECT 1, SET(@USER_ID, SCOPE_IDENTITY());
INSERT INTO UM_USER_ROLE (UM_ROLE_ID, UM_USER_ID ,UM_TENANT_ID )  VALUES (1, @USER_ID,-1234);
INSERT INTO UM_USER_ROLE (UM_ROLE_ID, UM_USER_ID ,UM_TENANT_ID )  VALUES (2,@USER_ID,-1234);

