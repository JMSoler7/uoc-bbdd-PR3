SET search_path TO ubd_20251;
CREATE OR REPLACE FUNCTION get_grape_count_wine(
    p_id_wine INTEGER
)
RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO v_count FROM WINE_GRAPE WHERE wine_id = p_id_wine;


    RETURN v_count;
END;
$$ LANGUAGE plpgsql ;