-- vendor =================================================================
INSERT INTO vendor VALUES(1, 'パマソニック');
INSERT INTO vendor VALUES(2, '西芝');
INSERT INTO vendor VALUES(3, '月立');
INSERT INTO vendor VALUES(4, '四菱');
INSERT INTO vendor VALUES(5, 'ショープ');

-- category =================================================================
INSERT INTO category VALUES(1, '冷蔵庫');
INSERT INTO category VALUES(4, 'エアコン');
INSERT INTO category VALUES(5, 'テレビ');

-- product =================================================================
INSERT INTO product VALUES(1, 'パマ冷蔵庫', 100000, 1, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(2, '西芝冷蔵庫', 80000, 2, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(3, '月立冷蔵庫', 150000, 3, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(4, '四菱冷蔵庫', 200000, 4, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(5, 'ショープ冷蔵庫', 50000, 5, 1, 'user00', CURRENT_TIMESTAMP);

INSERT INTO product VALUES(6, 'パマ冷蔵庫2', 100000, 1, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(7, '西芝冷蔵庫2', 80000, 2, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(8, '月立冷蔵庫2', 150000, 3, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(9, '四菱冷蔵庫2', 200000, 4, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(10, 'ショープ冷蔵庫2', 50000, 5, 1, 'user00', CURRENT_TIMESTAMP);

INSERT INTO product VALUES(11, 'パマ冷蔵庫3', 100000, 1, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(12, '西芝冷蔵庫3', 80000, 2, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(13, '月立冷蔵庫3', 150000, 3, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(14, '四菱冷蔵庫3', 200000, 4, 1, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(15, 'ショープ冷蔵庫3', 50000, 5, 1, 'user00', CURRENT_TIMESTAMP);

INSERT INTO product VALUES(16, 'デェーガ', 100000, 1, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(17, 'レゴザ', 80000, 2, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(18, 'ウォー', 150000, 3, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(19, 'レアル', 200000, 4, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(20, 'アスオク', 50000, 5, 5, 'user00', CURRENT_TIMESTAMP);

INSERT INTO product VALUES(21, 'デェーガ2', 100000, 1, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(22, 'レゴザ2', 80000, 2, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(23, 'ウォー2', 150000, 3, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(24, 'レアル2', 200000, 4, 5, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(25, 'アスオク2', 50000, 5, 5, 'user00', CURRENT_TIMESTAMP);

INSERT INTO product VALUES(26, 'パマエアコン', 100000, 1, 4, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(27, '西芝エアコン', 80000, 2, 4, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(28, '月立エアコン', 150000, 3, 4, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(29, '四菱エアコン', 200000, 4, 4, 'user00', CURRENT_TIMESTAMP);
INSERT INTO product VALUES(30, 'ショープエアコン', 50000, 5, 4, 'user00', CURRENT_TIMESTAMP);

-- customer =================================================================
INSERT INTO customer VALUES(1, '顧客A社', '03-xxxx-xxxx', '東京都港区');
INSERT INTO customer VALUES(2, '顧客B社', '03-yyyy-yyyy', '東京都千代田区');
INSERT INTO customer VALUES(3, '顧客C社', '03-zzzz-zzzz', '東京都中央区');

-- order_summary =================================================================
INSERT INTO order_summary VALUES(1, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(2, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(3, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(4, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(5, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(6, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(7, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(8, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(9, '2017-04-01 10:00:00', 1);
INSERT INTO order_summary VALUES(10, '2017-04-01 10:00:00', 1);

-- order_detail =================================================================
INSERT INTO order_detail VALUES(1, 1, 1, 2);
INSERT INTO order_detail VALUES(2, 1, 2, 2);
INSERT INTO order_detail VALUES(3, 1, 3, 2);
INSERT INTO order_detail VALUES(4, 2, 1, 2);
INSERT INTO order_detail VALUES(5, 2, 2, 2);
INSERT INTO order_detail VALUES(6, 2, 2, 2);
