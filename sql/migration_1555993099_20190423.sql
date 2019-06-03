SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;-- 	DROP TABLE IF EXISTS public.user_preference_type;-- 	CREATE TABLE public.user_preference_type-- 	(-- 	  id serial,-- 		code character varying(50) NOT NULL,-- 	  created_at timestamp without time zone DEFAULT now(),-- 	  updated_at timestamp without time zone DEFAULT now(),-- 	  CONSTRAINT user_preference_type_pkey PRIMARY KEY (id),-- 	  CONSTRAINT user_preference_type_code_ukey UNIQUE (code)-- 	);---- 	INSERT INTO public.user_preference_type(code) VALUES ('SHIPPING_ADDRESS');-- 	INSERT INTO public.user_preference_type(code) VALUES ('PAYMENT_OPTION');--    DROP TABLE IF EXISTS public.user_shipping_preference;	CREATE TABLE public.user_shipping_preference	(	  id serial,	  user_id int NOT NULL REFERENCES "user"(id),      first_name character varying(50) NOT NULL,      last_name character varying(50) NOT NULL,      email character varying(200) NOT NULL,      phone character varying(50) NOT NULL,	  address1 character varying(50) NOT NULL,	  address2 character varying(50),	  post_code character varying(50) NOT NULL,	  created_at timestamp without time zone DEFAULT now(),	  updated_at timestamp without time zone DEFAULT now(),	  CONSTRAINT user_shipping_preference_pkey PRIMARY KEY (id),	  CONSTRAINT user_shipping_preference_user_id_ukey UNIQUE (user_id)	);	COMMIT;