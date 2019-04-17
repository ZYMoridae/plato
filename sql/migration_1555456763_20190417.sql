SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;	DROP TABLE IF EXISTS public.wish_list;	CREATE TABLE public.wish_list	(		id serial,	  user_id int NOT NULL REFERENCES "user"(id),	  created_at timestamp without time zone DEFAULT now(),	  updated_at timestamp without time zone DEFAULT now(),	  CONSTRAINT wish_list_pkey PRIMARY KEY (id)	);	DROP TABLE IF EXISTS public.wish_list_item;	CREATE TABLE public.wish_list_item	(	  id serial,	  wish_list_id int NOT NULL REFERENCES "wish_list"(id),	  product_id int NOT NULL REFERENCES "product"(id),	  quantity int NOT NULL,	  created_at timestamp without time zone DEFAULT now(),	  updated_at timestamp without time zone DEFAULT now(),	  CONSTRAINT wish_list_item_pkey PRIMARY KEY (id),	  CONSTRAINT wish_list_product_ukey UNIQUE (wish_list_id, product_id)	);COMMIT;