CREATE TABLE public.products (
    id SERIAL PRIMARY KEY,
    product_name text null,
    product_price DECIMAL(8,2) null
);

CREATE INDEX ix_products_name ON public.products (product_name);

INSERT INTO public.products ( product_name, product_price ) VALUES ('product_'||'1',59.9);
INSERT INTO public.products ( product_name, product_price ) VALUES ('product_'||'2',49.9);
INSERT INTO public.products ( product_name, product_price ) VALUES ('product_'||'3',69.9);