SET search_path TO ubd_20251;
CREATE OR REPLACE VIEW premium_wines AS
SELECT
    w.wine_name,
    t.winery_name,
    p.pdo_name,
    w.vintage,
    w.prizes,
    w.price,
    TO_DATE(CAST(w.vintage AS VARCHAR), 'YYYY') + INTERVAL '15 years' AS BestBeforeDate
FROM
    WINE w
NATURAL JOIN
    WINERY t 
NATURAL JOIN
    PDO p 
WHERE
    w.category IN ('reserve', 'grand reserve')
    AND w.price > 30.00
    AND w.prizes > 4;