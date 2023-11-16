-- 11.8
-- 메인 상품 5가지 가져오기 
SELECT p.*  
FROM (SELECT rownum AS row_no, m.*  
      FROM (
             SELECT *
          FROM product_tbl            
           ) m  
      ) p  
WHERE p.row_no in ( 1, 3, 10, 12, 30 );

-- 11.8
-- 특정 상품 rownum으로 검색시 빈상품(무효 상품)인지 점검
SELECT count(decode(p.product_thumnail,'mall.gif','true'))    
FROM (SELECT rownum AS row_no, m.*  
      FROM (
             SELECT *
          FROM product_tbl            
           ) m  
      ) p  
WHERE p.row_no = 61;
